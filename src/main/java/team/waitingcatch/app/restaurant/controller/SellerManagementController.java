package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.restaurant.dto.DemandSignUpSellerControllerRequest;
import team.waitingcatch.app.restaurant.dto.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.GetDemandSignUpSellerResponse;
import team.waitingcatch.app.restaurant.service.requestseller.SellerManagementService;

@RestController
@RequiredArgsConstructor
public class SellerManagementController {

	private final SellerManagementService sellerManagementService;

	//판매자 권한 부분

	//판매자 셀러 요청
	@PostMapping(path = "/seller/demand")
	public void demandSignUpSeller(@RequestBody DemandSignUpSellerControllerRequest demandSignUpControllerRequest) {
		Address address = new Address(
			demandSignUpControllerRequest.getProvince(),
			demandSignUpControllerRequest.getCity(),
			demandSignUpControllerRequest.getStreet()
		);
		Position position = new Position(
			demandSignUpControllerRequest.getLatitude(),
			demandSignUpControllerRequest.getLongitude()
		);

		DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest = new DemandSignUpSellerServiceRequest(
			demandSignUpControllerRequest, address, position);
		sellerManagementService.demandSignUpSeller(demandSignupSellerServiceRequest);
	}

	//관리자 권한 부분

	//판매자 요청 조회
	@GetMapping(path = "/admin/seller-managements")
	public List<GetDemandSignUpSellerResponse> getDemandSignUpSellers() {
		return sellerManagementService.getDemandSignUpSellers();
	}
}


