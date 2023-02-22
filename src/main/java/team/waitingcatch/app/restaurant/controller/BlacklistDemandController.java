package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.BlacklistDemandControllerRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.DemandBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistDemandResponse;
import team.waitingcatch.app.restaurant.service.requestblacklist.BlacklistDemandService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@Controller
@RequiredArgsConstructor
public class BlacklistRequestController {
	private final BlacklistDemandService blackListDemandService;

	@PostMapping("/blacklists")
	public void requestUBlacklistByRestaurant(
		@Valid @ModelAttribute BlacklistDemandControllerRequest controllerRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new DemandBlacklistByRestaurantServiceRequest(userDetails.getId(),
			controllerRequest.getUserId(), controllerRequest.getDescription());
		blackListDemandService.submitBlacklistDemand(serviceRequest);
	}

	@PutMapping("/blacklists/{blacklistId}")
	public void cancelBlacklistByRestaurant(
		@PathVariable Long blacklistId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new CancelBlacklistDemandServiceRequest(blacklistId, userDetails.getId());
		blackListDemandService.cancelBlacklistDemand(serviceRequest);
	}
	@GetMapping("/admin/restaurants/blacklist-request")
	public List<GetBlacklistDemandResponse> getRequestBlacklists() {
		return blackListDemandService.getBlacklistDemands();
	}

	@PostMapping("/admin/restaurants/blacklist-request/{blacklistRequestId}")
	public void approveBlacklistRequest(@PathVariable Long blacklistRequestId) {
		var serviceRequest = new ApproveBlacklistDemandServiceRequest(blacklistRequestId);
		blackListDemandService.approveBlacklistDemand(serviceRequest);
	}
}