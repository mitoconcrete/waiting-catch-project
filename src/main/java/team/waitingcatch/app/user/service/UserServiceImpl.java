package team.waitingcatch.app.user.service;

import static team.waitingcatch.app.exception.ErrorCode.*;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.JwtUtil;
import team.waitingcatch.app.event.service.event.InternalEventService;
import team.waitingcatch.app.exception.AlreadyExistsException;
import team.waitingcatch.app.lineup.service.InternalLineupHistoryService;
import team.waitingcatch.app.lineup.service.InternalLineupService;
import team.waitingcatch.app.lineup.service.InternalReviewService;
import team.waitingcatch.app.redis.dto.CreateRefreshTokenServiceRequest;
import team.waitingcatch.app.redis.dto.KillTokenRequest;
import team.waitingcatch.app.redis.service.AliveTokenService;
import team.waitingcatch.app.redis.service.KilledAccessTokenService;
import team.waitingcatch.app.redis.service.RemoveTokenRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;
import team.waitingcatch.app.user.dto.CreateUserServiceRequest;
import team.waitingcatch.app.user.dto.DeleteUserRequest;
import team.waitingcatch.app.user.dto.FindPasswordRequest;
import team.waitingcatch.app.user.dto.GetCustomerByIdAndRoleServiceRequest;
import team.waitingcatch.app.user.dto.LoginRequest;
import team.waitingcatch.app.user.dto.LoginServiceResponse;
import team.waitingcatch.app.user.dto.LogoutRequest;
import team.waitingcatch.app.user.dto.UpdatePasswordServiceRequest;
import team.waitingcatch.app.user.dto.UpdateUserServiceRequest;
import team.waitingcatch.app.user.dto.UserInfoResponse;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, InternalUserService {

	private final UserRepository userRepository;
	private final AliveTokenService refreshTokenService;
	private final KilledAccessTokenService accessTokenService;
	private final InternalRestaurantService internalRestaurantService;
	private final InternalLineupService internalLineupService;
	private final InternalLineupHistoryService internalLineupHistoryService;
	private final InternalEventService internalEventService;
	private final InternalReviewService internalReviewService;

	private final JwtUtil jwtUtil;
	private final JavaMailSender emailSender;

	@Value("${spring.mail.username}")
	private String smtpSenderEmail;

	@Override
	public LoginServiceResponse login(LoginRequest payload) {
		User user = _getUserByUsername(payload.getUsername());

		if (!user.isPasswordMatch(payload.getPassword())) {
			throw new IllegalArgumentException(INCORRECT_PASSWORD.getMessage());
		}

		String accessToken = _createAccessTokensByUser(user);

		return new LoginServiceResponse(accessToken);
	}

	@Override
	public void logout(LogoutRequest payload) {
		KillTokenRequest servicePayload = new KillTokenRequest(payload.getAccessToken());

		// refresh Token ??????
		refreshTokenService.removeToken(new RemoveTokenRequest(servicePayload.getAccessToken()));

		// kill token ???????????? ??????.
		accessTokenService.killToken(servicePayload);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserInfoResponse> getCustomers(Pageable payload) {
		Page<User> result = userRepository.findAll(payload);
		return new PageImpl<>(result.getContent().stream().map(UserInfoResponse::new).collect(Collectors.toList()),
			payload, result.getTotalElements());
	}

	@Override
	@Transactional(readOnly = true)
	public UserInfoResponse getByUserIdAndRole(GetCustomerByIdAndRoleServiceRequest payload) {
		// ????????? ??????????????? ????????????.
		User user = userRepository.findById(payload.getUserId())
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_USER.getMessage()));

		// ???????????? ?????? ????????? ?????? ????????? ????????? ????????????.
		if (!user.hasSameRole(payload.getRole())) {
			throw new NoSuchElementException(NOT_FOUND_USER.getMessage());
		}

		return new UserInfoResponse(user);
	}

	@Override
	public void createUser(CreateUserServiceRequest payload) {
		// ?????? ???????????? ???????????? ???????????????.
		boolean isExistUser = userRepository.existsByUsername(payload.getUsername());

		if (isExistUser) {
			throw new AlreadyExistsException(ALREADY_EXISTS_USERNAME);
		}

		// ????????? ????????? ???????????? ???????????????.
		User newUser =
			new User(
				payload.getRole(),
				payload.getName(),
				payload.getEmail(),
				payload.getUsername(),
				payload.getPassword(),
				payload.getNickname(),
				payload.getPhoneNumber()
			);

		userRepository.save(newUser);
	}

	@Override
	public void updateUser(UpdateUserServiceRequest payload) {
		// ???????????? ????????? ???(?????????, boolean, ?????????)?????? ???????????????.
		User user = _getUserByUsername(payload.getUsername());
		user.updateBasicInfo(payload.getNickName(), payload.getName(), payload.getPhoneNumber(), payload.getEmail());
	}

	@Override
	@Transactional
	public void findUserAndSendEmail(FindPasswordRequest payload) {
		User user = userRepository.findByUsernameAndEmailAndIsDeletedFalse(payload.getUsername(), payload.getEmail())
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_USER.getMessage()));

		// ????????? ???????????? ?????? ??????????????? ????????????, ????????????.
		String temporaryPassword = UUID.randomUUID().toString().substring(0, 10);
		user.updatePassword(temporaryPassword);

		// ????????? ????????? ???????????? ????????? ????????????.
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(smtpSenderEmail);
		message.setTo(user.getEmail());
		message.setSubject("[WAITING CATCH] ?????? ???????????? ??????");
		message.setText("??????????????? ????????? WAITING CATCH ?????????.\n???????????? ?????? ??????????????? ??????????????????.\n" + temporaryPassword
			+ "\n????????? ??? ??????????????? ??? ????????? ?????????.");
		emailSender.send(message);
	}

	@Override
	public LoginServiceResponse createAccessTokenByEmail(String email) {
		User user = _getUserByEmail(email);

		String accessToken = _createAccessTokensByUser(user);

		return new LoginServiceResponse(accessToken);
	}

	@Override
	public void deleteCustomer(DeleteUserRequest payload) {
		User user = _getUserByUsername(payload.getUsername());
		userRepository.deleteById(user.getId());
	}

	@Override
	public void deleteSeller(DeleteUserRequest payload) {
		User user = _getUserByUsername(payload.getUsername());
		_deleteSellerAndRelatedInformation(user.getId());
	}

	@Override
	public void updatePassword(UpdatePasswordServiceRequest payload) {
		User user = _getUserByUsername(payload.getUsername());
		user.updatePassword(payload.getPassword());
	}

	@Override
	public void _deleteSellerAndRelatedInformation(Long userId) {
		User seller = _getUserByUserId(userId);
		userRepository.deleteById(seller.getId());
		Restaurant restaurant = internalRestaurantService._deleteRestaurantBySellerId(seller.getId());
		internalLineupHistoryService._bulkSoftDeleteByRestaurantId(restaurant.getId());
		internalLineupService._bulkSoftDeleteByRestaurantId(restaurant.getId());
		internalEventService._bulkSoftDeleteByRestaurantId(restaurant.getId());
		internalReviewService._bulkSoftDeleteByRestaurantId(restaurant.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public String _getUsernameById(Long id) {
		return userRepository.findUsernameById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public User _getUserByUsername(String username) {
		return userRepository.findByUsernameAndIsDeletedFalse(username).orElseThrow(
			() -> new NoSuchElementException(NOT_FOUND_USER.getMessage()));
	}

	@Override
	@Transactional(readOnly = true)
	public User _getUserByEmail(String email) {
		return userRepository.findByEmailAndIsDeletedFalse(email)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_USER.getMessage()));
	}

	@Override
	@Transactional(readOnly = true)
	public User _getUserByUserId(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(NOT_FOUND_USER.getMessage()));
	}

	private String _createAccessTokensByUser(User user) {
		// access token ??? refresh token??? ???????????????.
		String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole());
		String refreshToken = jwtUtil.createRefreshToken(user.getUsername(), user.getRole());

		/*
		refreshToken??? redis??? ???????????????. ??? ???, accessToken:RefreshToken??? ????????? ????????????, 'Bearer ' ??? ????????? ????????? ???????????????.
		1. refreshToken??? accessToken??? value??? ???????????? ????????? ???????????? ?????? ???,
		   ????????? accessToken ????????? claim??? ????????? ??? ?????? ???????????????.
		2. accessToken ????????? key??? ?????? ????????? redis?????? ?????? ?????? ????????? ???????????? ???????????????.
		*/
		CreateRefreshTokenServiceRequest servicePayload = new CreateRefreshTokenServiceRequest(accessToken.substring(7),
			refreshToken.substring(7), JwtUtil.REFRESH_TOKEN_TIME);
		refreshTokenService.createToken(servicePayload);
		return accessToken;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserInfoResponse> getCustomersByUserName(String searchVal, Pageable pageable) {
		Page<User> users = userRepository.findByUsernameContaining(searchVal,
			pageable);
		return new PageImpl<>(userRepository.findByUsernameContaining(searchVal, pageable)
			.getContent()
			.stream()
			.map(UserInfoResponse::new)
			.collect(Collectors.toList()),
			pageable, users.getTotalElements());
	}
}