package team.waitingcatch.app.restaurant.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.BlacklistDemandControllerRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListDemandByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistDemandResponse;
import team.waitingcatch.app.restaurant.service.requestblacklist.BlacklistDemandService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@Controller
@RequiredArgsConstructor
public class BlacklistDemandController {
	private final BlacklistDemandService blacklistDemandService;

	@PostMapping("/blacklist-demands")
	public void createBlacklistDemand(
		@Valid @ModelAttribute BlacklistDemandControllerRequest controllerRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new CreateBlacklistDemandServiceRequest(userDetails.getId(),
			controllerRequest.getUserId(), controllerRequest.getDescription());
		blacklistDemandService.submitBlacklistDemand(serviceRequest);
	}

	@DeleteMapping("/blacklist-demands/{blacklistDemandId}")
	@ResponseBody
	public void cancelBlacklistDemand(
		@PathVariable Long blacklistDemandId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new CancelBlacklistDemandServiceRequest(blacklistDemandId, userDetails.getId());
		blacklistDemandService.cancelBlacklistDemand(serviceRequest);
	}

	@GetMapping("/admin/restaurants/blacklist-demands")
	public GenericResponse<GetBlacklistDemandResponse> getBlacklistDemands(
		@PageableDefault(size = 10, page = 0) Pageable pageable) {
		return new GenericResponse(blacklistDemandService.getBlacklistDemands(pageable));
	}

	@PostMapping("/admin/restaurants/blacklist-demands/{blacklistDemandId}")
	public void approveBlacklistDemand(@PathVariable Long blacklistDemandId) {
		var serviceRequest = new ApproveBlacklistDemandServiceRequest(blacklistDemandId);
		blacklistDemandService.approveBlacklistDemand(serviceRequest);
	}

	@GetMapping("/seller/restaurant/blacklist-demands")
	public String getBlackListDemandByRestaurant(
		Model model,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new GetBlackListDemandByRestaurantServiceRequest(userDetails.getId());
		model.addAttribute("blacklistDemandList",
			blacklistDemandService.getBlackListDemandsByRestaurant(serviceRequest));
		return "seller/blacklist-demand-list";
	}

	@GetMapping("/seller/restaurant/blacklist-demand-page")
	public String getBlacklistDemandPage() {
		return "seller/blacklist-demand";
	}
}