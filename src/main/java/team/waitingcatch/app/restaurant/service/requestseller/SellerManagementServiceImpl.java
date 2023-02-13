package team.waitingcatch.app.restaurant.service.requestseller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.GetDemandSignUpSellerResponse;
import team.waitingcatch.app.restaurant.entity.SellerManagement;
import team.waitingcatch.app.restaurant.repository.SellerManagementRepository;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.service.InternalUserService;

@Service
@RequiredArgsConstructor
@Transactional
public class SellerManagementServiceImpl implements SellerManagementService, InternalSellerManagementService {

	private final SellerManagementRepository sellerManagementRepository;
	private final InternalUserService internalUserService;

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

}
