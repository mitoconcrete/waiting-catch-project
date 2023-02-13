package team.waitingcatch.app.restaurant.service.requestseller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest;
import team.waitingcatch.app.restaurant.dto.ApproveSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.ApproveSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.GetDemandSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.RejectSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.SellerManagement;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.restaurant.repository.SellerManagementRepository;
import team.waitingcatch.app.user.dto.UserCreateServiceRequest;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.service.InternalUserService;
import team.waitingcatch.app.user.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional
public class SellerManagementServiceImpl implements SellerManagementService, InternalSellerManagementService {
	private final SellerManagementRepository sellerManagementRepository;
	private final InternalUserService internalUserService;
	private final RestaurantRepository restaurantRepository;
	private final UserService userService;

	//판매자 요청 등록 하는 메소드
	public void demandSignUpSeller(DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest) {
		User user = internalUserService._findByUsername(demandSignupSellerServiceRequest.getUsername());
		if (user != null) {
			throw new IllegalArgumentException("Duplicated user");
		}
		User userEmail = internalUserService._findByEmail(demandSignupSellerServiceRequest.getEmail());
		if (userEmail != null) {
			throw new IllegalArgumentException("Duplicated email");
		}

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
		if (sellerManagement.getStatus() == AcceptedStatusEnum.REJECTION) {
			throw new IllegalArgumentException("This request already rejected please request seller again");
		}
		sellerManagement.approveUpdateStatus();
		sellerManagementRepository.save(sellerManagement);

		//비밀번호
		String uuidPassword = UUID.randomUUID().toString().substring(1, 8);

		//회원가입
		UserCreateServiceRequest
			userCreateServiceRequest
			= new UserCreateServiceRequest(sellerManagement, uuidPassword);

		User seller = userService.createSeller(userCreateServiceRequest);

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
		///오류날까?
		sellerManagement.rejectUpdateStatus();
		sellerManagementRepository.save(sellerManagement);

	}

}
