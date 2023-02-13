package team.waitingcatch.app.restaurant.service.requestseller;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.DemandSignUpSellerServiceRequest;
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

	//판매자가 요청을함 -> 요청내용을 저장 -> 중복검사해야함 -> 중복검사 통과 -> sellermanagement 엔티티에 저장
	//사람이 하기 (1) or api가 하기
	//그 상태를 어드민이 approve // reject 시켜주는 메소드 구현
	//approve와 동시에 회원가입 진행하고 비밀번호 리턴 (api).

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

}
