package team.waitingcatch.app.restaurant.controller;

import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerControllerRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.GetDemandSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.requestseller.GetRequestSellerByRestaurantRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.GetRequestSellerControllerRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.RejectSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.service.requestseller.SellerManagementService;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class SellerManagementController {

	private final SellerManagementService sellerManagementService;

	//판매자 권한 부분

	//판매자 셀러 요청
	@PostMapping("/seller/demand")
	public void demandSignUpSeller(
		@Valid @RequestBody DemandSignUpSellerControllerRequest demandSignUpControllerRequest) {
		// Address address = new Address(
		// 	demandSignUpControllerRequest.getProvince(),
		// 	demandSignUpControllerRequest.getCity(),
		// 	demandSignUpControllerRequest.getStreet()
		// );
		// Position position = new Position(
		// 	demandSignUpControllerRequest.getLatitude(),
		// 	demandSignUpControllerRequest.getLongitude()
		// );

		DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest = new DemandSignUpSellerServiceRequest(
			demandSignUpControllerRequest);
		sellerManagementService.demandSignUpSeller(demandSignupSellerServiceRequest);
	}

	@GetMapping("/seller/seller-management")
	public GetDemandSignUpSellerResponse getRequestSellerByRestaurant(
		@RequestBody GetRequestSellerControllerRequest getRequestSellerControllerRequest) {
		GetRequestSellerByRestaurantRequest getRequestSellerByRestaurantRequest = new GetRequestSellerByRestaurantRequest(
			getRequestSellerControllerRequest.getRequestSellerName(), getRequestSellerControllerRequest.getEmail());
		return sellerManagementService.getRequestSellerByRestaurant(getRequestSellerByRestaurantRequest);
	}

	//관리자 권한 부분

	//판매자 요청 조회
	@GetMapping("/admin/seller-management")
	public ModelAndView sellerManagementPage(Model model) {
		model.addAttribute("requestSeller", sellerManagementService.getDemandSignUpSellers());

		return new ModelAndView("/admin/seller-management");
	}

	@PostMapping("/admin/seller-managements/{sellerManagementId}")
	public ApproveSignUpSellerResponse approveSignUpSeller(@PathVariable Long sellerManagementId) {
		ApproveSignUpSellerServiceRequest approveSignUpSellerServiceRequest = new ApproveSignUpSellerServiceRequest(
			sellerManagementId);

		return sellerManagementService.approveSignUpSeller(approveSignUpSellerServiceRequest);
	}

	@PutMapping("/admin/seller-managements/{sellerManagementId}")
	public void rejectSignUpSeller(@PathVariable Long sellerManagementId) {
		RejectSignUpSellerServiceRequest rejectSignUpSellerServiceRequest = new RejectSignUpSellerServiceRequest(
			sellerManagementId);
		sellerManagementService.rejectSignUpSeller(rejectSignUpSellerServiceRequest);
	}

}


