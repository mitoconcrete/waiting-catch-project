package team.waitingcatch.app.restaurant.service.requestseller;

import static team.waitingcatch.app.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;
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
import team.waitingcatch.app.exception.AlreadyExistsException;
import team.waitingcatch.app.lineup.entity.WaitingNumber;
import team.waitingcatch.app.lineup.repository.WaitingNumberRepository;
import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.ConnectCategoryRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.GetDemandSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.requestseller.GetRequestSellerByRestaurantRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.RejectSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.RestaurantInfo;
import team.waitingcatch.app.restaurant.entity.SellerManagement;
import team.waitingcatch.app.restaurant.repository.RestaurantInfoRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.restaurant.repository.SellerManagementRepository;
import team.waitingcatch.app.restaurant.service.category.InternalCategoryService;
import team.waitingcatch.app.user.dto.CreateUserServiceRequest;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.enums.UserRoleEnum;
import team.waitingcatch.app.user.repository.UserRepository;
import team.waitingcatch.app.user.service.InternalUserService;
import team.waitingcatch.app.user.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional
public class SellerManagementServiceImpl implements SellerManagementService, InternalSellerManagementService {
	private final SellerManagementRepository sellerManagementRepository;
	private final UserRepository userRepository;
	private final RestaurantRepository restaurantRepository;
	private final RestaurantInfoRepository restaurantInfoRepository;
	private final WaitingNumberRepository waitingNumberRepository;
	private final InternalCategoryService categoryService;

	private final InternalUserService internalUserService;
	private final UserService userService;
	private final JavaMailSender emailSender;

	@Value("${spring.mail.username}")
	private String smtpSenderEmail;

	//????????? ?????? ?????? ?????? ?????????
	public void demandSignUpSeller(DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest) {
		boolean isExists = userRepository.existsByUsername(demandSignupSellerServiceRequest.getUsername());
		if (isExists) {
			throw new AlreadyExistsException(ALREADY_EXISTS_USERNAME);
		}

		SellerManagement sellerManagement = new SellerManagement(demandSignupSellerServiceRequest);
		sellerManagementRepository.save(sellerManagement);
	}

	//????????? ?????? ????????? ??????
	@Transactional(readOnly = true)
	public Page<GetDemandSignUpSellerResponse> getDemandSignUpSellers(Pageable pageable) {
		Page<SellerManagement> sellerManagement = sellerManagementRepository.findAll(pageable);
		return new PageImpl<>(sellerManagementRepository.findAll(pageable)
			.getContent()
			.stream()
			.map(GetDemandSignUpSellerResponse::new)
			.collect(Collectors.toList()),
			pageable, sellerManagement.getTotalElements());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetDemandSignUpSellerResponse> getDemandSignUpSellersById(String searchVal, Pageable pageable) {
		Page<SellerManagement> sellerManagement = sellerManagementRepository.findByUsernameContaining(searchVal,
			pageable);
		return new PageImpl<>(sellerManagementRepository.findByUsernameContaining(searchVal, pageable)
			.getContent()
			.stream()
			.map(GetDemandSignUpSellerResponse::new)
			.collect(Collectors.toList()),
			pageable, sellerManagement.getTotalElements());
	}

	public boolean approveSignUpSeller(ApproveSignUpSellerServiceRequest serviceRequest) {
		SellerManagement sellerManagement = sellerManagementRepository.findById(serviceRequest.getId())
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_SELLER_REQUEST.getMessage()));

		boolean checkReject = sellerManagement.checkReject();
		boolean checkApprove = sellerManagement.checkApprove();

		if (!checkReject || !checkApprove) {
			return false;
		}

		sellerManagement.approveUpdateStatus();
		//????????????
		String uuidPassword = UUID.randomUUID().toString().substring(1, 8);
		//????????????
		CreateUserServiceRequest
			userCreateServiceRequest
			= new CreateUserServiceRequest(UserRoleEnum.SELLER, sellerManagement.getName(),
			sellerManagement.getEmail(),
			sellerManagement.getUsername(), uuidPassword, null, sellerManagement.getPhoneNumber());
		userService.createUser(userCreateServiceRequest);

		User seller = internalUserService._getUserByUsername(sellerManagement.getUsername());
		// ???????????? ?????????
		List<Long> categoryIds = sellerManagement.getCategories().stream()
			.map(Long::parseLong)
			.collect(Collectors.toList());

		List<String> searchKeywords = categoryService._getCategoryNames(categoryIds);

		var entityRequest = new ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest(sellerManagement,
			seller, searchKeywords);

		Restaurant restaurant = new Restaurant(entityRequest);
		restaurantRepository.save(restaurant);

		var request = new ConnectCategoryRestaurantServiceRequest(
			restaurant, sellerManagement.getCategories());
		categoryService._connectCategoryRestaurant(request);

		RestaurantInfo restaurantInfo = new RestaurantInfo(restaurant);
		restaurantInfoRepository.save(restaurantInfo);

		WaitingNumber waitingNumber = WaitingNumber.of(restaurant);
		waitingNumberRepository.save(waitingNumber);

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(smtpSenderEmail);
		message.setTo(sellerManagement.getEmail());
		message.setSubject("[WAITING CATCH] ????????? ?????? ??????");
		message.setText("??????????????? WAITING CATCH ?????????.\n ???????????? ????????? ??? ?????? ??????????????? ??????????????????.\n" +
			"ID: " + sellerManagement.getUsername() + "\nPW:" + uuidPassword + "\n????????? ??? ??????????????? ??? ????????? ?????????.");
		emailSender.send(message);
		return true;
	}

	public boolean rejectSignUpSeller(RejectSignUpSellerServiceRequest rejectSignUpSellerServiceRequest) {
		SellerManagement sellerManagement = sellerManagementRepository.findById(
				rejectSignUpSellerServiceRequest.getId())
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_SELLER_REQUEST.getMessage()));

		boolean checkReject = sellerManagement.checkReject();
		boolean checkApprove = sellerManagement.checkApprove();

		if (!checkReject || !checkApprove) {
			return false;
		}

		sellerManagement.rejectUpdateStatus();

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(smtpSenderEmail);
		message.setTo(sellerManagement.getEmail());
		message.setSubject("[WAITING CATCH] ????????? ?????? ??????");
		message.setText("??????????????? WAITING CATCH ?????????.\n ???????????? ????????? ????????? ?????? ????????? ???????????? ????????? ?????????????????????."
			+ "\n?????? ??? ????????? ??????????????????. ???????????????.");
		emailSender.send(message);
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public GetDemandSignUpSellerResponse getRequestSellerByRestaurant(GetRequestSellerByRestaurantRequest request) {
		SellerManagement sellerManagement = sellerManagementRepository.findTopByUsernameAndEmailOrderByCreatedDateDesc(
			request.getRequestSellerName(), request.getEmail());
		return new GetDemandSignUpSellerResponse(sellerManagement);
	}
}