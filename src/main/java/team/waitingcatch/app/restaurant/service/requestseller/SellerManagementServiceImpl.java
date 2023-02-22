package team.waitingcatch.app.restaurant.service.requestseller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.GetDemandSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.requestseller.RejectSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.SellerManagement;
import team.waitingcatch.app.restaurant.repository.RestaurantInfoRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.restaurant.repository.SellerManagementRepository;
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
	private final InternalUserService internalUserService;
	private final RestaurantRepository restaurantRepository;
	private final RestaurantInfoRepository restaurantInfoRepository;
	private final UserService userService;

	private final UserRepository userRepository;

	//판매자 요청 등록 하는 메소드
	public void demandSignUpSeller(DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest) {
		boolean user = userRepository.existsByUsername(demandSignupSellerServiceRequest.getUsername());
		if (user) {
			throw new IllegalArgumentException("해당 사용자가 존재합니다.");
		}
		//internalUserService._getUserByUsername(demandSignupSellerServiceRequest.getUsername());
		//internalUserService._getUserByEmail(demandSignupSellerServiceRequest.getEmail());

		SellerManagement sellerManagement = new SellerManagement(demandSignupSellerServiceRequest);
		sellerManagementRepository.save(sellerManagement);
	}

	//판매자 요청 리스트 조회
	@Transactional(readOnly = true)
	public List<GetDemandSignUpSellerResponse> getDemandSignUpSellers() {
		List<SellerManagement> sellerManagement = sellerManagementRepository.findAll();
		return sellerManagement.stream().map(GetDemandSignUpSellerResponse::new).collect(Collectors.toList());
	}

	public ApproveSignUpSellerResponse approveSignUpSeller(ApproveSignUpSellerServiceRequest
		approveSignUpSellerServiceRequest) {
		SellerManagement sellerManagement = sellerManagementRepository.findById(
				approveSignUpSellerServiceRequest.getId())
			.orElseThrow(() -> new IllegalArgumentException("Not found request seller sign-up"));

		sellerManagement.checkReject();
		sellerManagement.checkApprove();

		sellerManagement.approveUpdateStatus();
		//비밀번호
		String uuidPassword = UUID.randomUUID().toString().substring(1, 8);
		//회원가입
		CreateUserServiceRequest
			userCreateServiceRequest
			= new CreateUserServiceRequest(UserRoleEnum.SELLER, sellerManagement.getName(), sellerManagement.getEmail(),
			sellerManagement.getUsername(), uuidPassword, null, sellerManagement.getPhoneNumber());
		userService.createUser(userCreateServiceRequest);

		User seller = internalUserService._getUserByUsername(sellerManagement.getUsername());
		// 레스토랑 만들기
		ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest
			approveSignUpSellerManagementEntityPassToRestaurantEntityRequest
			= new ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest(sellerManagement, seller);
		Restaurant restaurant = new Restaurant(approveSignUpSellerManagementEntityPassToRestaurantEntityRequest);
		restaurantRepository.save(restaurant);
		return new ApproveSignUpSellerResponse(sellerManagement.getUsername(), uuidPassword);
	}

	public void rejectSignUpSeller(RejectSignUpSellerServiceRequest rejectSignUpSellerServiceRequest) {
		SellerManagement sellerManagement = sellerManagementRepository.findById(
				rejectSignUpSellerServiceRequest.getId())
			.orElseThrow(() -> new IllegalArgumentException("Not found request seller sign-up"));
		sellerManagement.checkReject();
		sellerManagement.checkApprove();
		sellerManagement.rejectUpdateStatus();
	}

}
