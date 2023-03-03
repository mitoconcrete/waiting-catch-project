package team.waitingcatch.app.user.service;

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
	private final JwtUtil jwtUtil;
	private final JavaMailSender emailSender;
	private final UserRepository userRepository;
	private final AliveTokenService refreshTokenService;
	private final KilledAccessTokenService accessTokenService;
	private final InternalRestaurantService internalRestaurantService;
	private final InternalLineupService internalLineupService;
	private final InternalLineupHistoryService internalLineupHistoryService;
	private final InternalEventService internalEventService;
	private final InternalReviewService internalReviewService;
	@Value("${spring.mail.username}")
	private String smtpSenderEmail;

	@Override
	public LoginServiceResponse login(LoginRequest payload) {
		User user = _getUserByUsername(payload.getUsername());

		if (!user.isPasswordMatch(payload.getPassword())) {
			throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
		}

		String accessToken = _createAccessTokensByUser(user);

		return new LoginServiceResponse(accessToken);
	}

	@Override
	public void logout(LogoutRequest payload) {
		KillTokenRequest servicePayload = new KillTokenRequest(payload.getAccessToken());

		// refresh Token 제거
		refreshTokenService.removeToken(new RemoveTokenRequest(servicePayload.getAccessToken()));

		// kill token 리스트에 추가.
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
		// 유저의 존재여부를 판단한다.
		User user = userRepository.findById(payload.getUserId()).orElseThrow(
			() -> new IllegalArgumentException("유저가 존재하지 않습니다.")
		);

		// 입력받은 롤과 동일한 롤을 지니고 있는지 확인한다.
		if (!user.hasSameRole(payload.getRole())) {
			throw new IllegalArgumentException("유저가 존재하지 않습니다.");
		}

		return new UserInfoResponse(user);
	}

	@Override
	public void createUser(CreateUserServiceRequest payload) {
		// 이미 존재하는 유저인지 검증합니다.
		boolean isExistUser = userRepository.existsByUsername(payload.getUsername());

		if (isExistUser) {
			throw new IllegalArgumentException("이미 존재하는 유저입니다.");
		}

		// 새로운 유저를 생성하고 저장합니다.
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
		// 중복되면 안되는 값(이메일, boolean, 닉네임)들을 체크해준다.
		User user = _getUserByUsername(payload.getUsername());
		user.updateBasicInfo(payload.getNickName(), payload.getName(), payload.getPhoneNumber(), payload.getEmail());
	}

	@Override
	@Transactional
	public void findUserAndSendEmail(FindPasswordRequest payload) {
		User user = userRepository.findByUsernameAndEmailAndIsDeletedFalse(payload.getUsername(), payload.getEmail())
			.orElseThrow(
				() -> new IllegalArgumentException("유저가 존재하지 않습니다.")
			);

		// 유저가 존재하면 임시 비밀번호를 생성하고, 저장한다.
		String temporaryPassword = UUID.randomUUID().toString().substring(0, 10);
		user.updatePassword(temporaryPassword);

		// 저장된 번호를 유저에게 메일로 전달한다.
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(smtpSenderEmail);
		message.setTo(user.getEmail());
		message.setSubject("임시패스워드 안내");
		message.setText("안녕하세요. 임시비밀번호 안내 관련 이메일 입니다. 회원님의 임시 비밀번호는 " +
			temporaryPassword + "입니다." + "\n로그인 후에 비밀번호를 변경을 해주세요.");
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
		userRepository.deleteById(user.getId());
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
			() -> new IllegalArgumentException("유저가 존재하지 않습니다.")
		);
	}

	@Override
	@Transactional(readOnly = true)
	public User _getUserByEmail(String email) {
		return userRepository.findByEmailAndIsDeletedFalse(email).orElseThrow(
			() -> new IllegalArgumentException("유저가 존재하지 않습니다.")
		);
	}

	@Override
	@Transactional(readOnly = true)
	public User _getUserByUserId(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
	}

	private String _createAccessTokensByUser(User user) {
		// access token 과 refresh token을 생성합니다.
		String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole());
		String refreshToken = jwtUtil.createRefreshToken(user.getUsername(), user.getRole());

		/*
		refreshToken을 redis에 저장합니다. 이 때, accessToken:RefreshToken의 형태로 저장하되, 'Bearer ' 을 제외한 토큰을 저장합니다.
		1. refreshToken을 accessToken의 value에 맵핑하는 이유는 시큐리티 검증 시,
		   만료된 accessToken 내에서 claim을 가져올 수 없기 때문입니다.
		2. accessToken 토큰을 key로 두는 이유는 redis내의 값에 좀더 빠르게 접근하기 위함입니다.
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
