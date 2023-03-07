package team.waitingcatch.app.restaurant.controller;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerControllerRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.GetDemandSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.requestseller.GetRequestSellerByRestaurantRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.GetRequestSellerControllerRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.RejectSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.service.requestseller.SellerManagementService;
import team.waitingcatch.app.restaurant.service.restaurant.MapApiService;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class SellerManagementController {
	private final SellerManagementService sellerManagementService;
	private final MapApiService mapApiService;

	// 판매자 권한 부분
	// 판매자 셀러 요청
	@PostMapping("/general/demand")
	public void demandSignUpSeller(
		@Valid @RequestBody DemandSignUpSellerControllerRequest demandSignUpControllerRequest) {
		Position position = mapApiService.getPosition(demandSignUpControllerRequest.getQuery());

		DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest = new DemandSignUpSellerServiceRequest(
			demandSignUpControllerRequest, position);
		sellerManagementService.demandSignUpSeller(demandSignupSellerServiceRequest);
	}

	@GetMapping("/seller/seller-management")
	public GenericResponse<GetDemandSignUpSellerResponse> getRequestSellerByRestaurant(
		@RequestBody GetRequestSellerControllerRequest getRequestSellerControllerRequest) {
		GetRequestSellerByRestaurantRequest getRequestSellerByRestaurantRequest = new GetRequestSellerByRestaurantRequest(
			getRequestSellerControllerRequest.getRequestSellerName(), getRequestSellerControllerRequest.getEmail());
		return new GenericResponse<>(
			sellerManagementService.getRequestSellerByRestaurant(getRequestSellerByRestaurantRequest));
	}

	// 관리자 권한 부분
	// 판매자 요청 조회
	@GetMapping("/admin/seller-management")
	public GenericResponse<Page<GetDemandSignUpSellerResponse>> sellerManagementPage(
		@PageableDefault Pageable pageable) {
		return new GenericResponse<>(sellerManagementService.getDemandSignUpSellers(pageable));
	}

	@PostMapping("/admin/seller-managements/{sellerManagementId}")
	public void approveSignUpSeller(@PathVariable Long sellerManagementId,
		HttpServletResponse response, Model model) throws
		IOException {
		ApproveSignUpSellerServiceRequest approveSignUpSellerServiceRequest = new ApproveSignUpSellerServiceRequest(
			sellerManagementId);
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		sellerManagementService.approveSignUpSeller(approveSignUpSellerServiceRequest);
	}

	@PutMapping("/admin/seller-managements/{sellerManagementId}")
	public void rejectSignUpSeller(@PathVariable Long sellerManagementId, HttpServletResponse response,
		Model model) throws
		IOException {
		RejectSignUpSellerServiceRequest rejectSignUpSellerServiceRequest = new RejectSignUpSellerServiceRequest(
			sellerManagementId);

		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		sellerManagementService.rejectSignUpSeller(rejectSignUpSellerServiceRequest);
	}
}