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
import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlacklistServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.DemandBlacklistByRestaurantControllerRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.DemandBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetRequestBlacklistResponse;
import team.waitingcatch.app.restaurant.service.requestblacklist.BlacklistRequestService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@Controller
@RequiredArgsConstructor
public class BlacklistRequestController {
	private final BlacklistRequestService blackListRequestService;

	@PostMapping("/blacklists")
	public void requestUBlacklistByRestaurant(
		@Valid @ModelAttribute DemandBlacklistByRestaurantControllerRequest controllerRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new DemandBlacklistByRestaurantServiceRequest(userDetails.getId(),
			controllerRequest.getUserId(), controllerRequest.getDescription());
		blackListRequestService.requestUserBlackList(serviceRequest);
	}

	@PutMapping("/blacklists/{blacklistId}")
	public void cancelBlacklistByRestaurant(
		@PathVariable Long blacklistId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new CancelBlacklistByRestaurantServiceRequest(blacklistId, userDetails.getId());
		blackListRequestService.cancelRequestUserBlacklist(serviceRequest);
	}
	@GetMapping("/admin/restaurants/blacklist-request")
	public List<GetRequestBlacklistResponse> getRequestBlacklists() {
		return blackListRequestService.getRequestBlacklist();
	}

	@PostMapping("/admin/restaurants/blacklist-request/{blacklistRequestId}")
	public void approveBlacklistRequest(@PathVariable Long blacklistRequestId) {
		var serviceRequest = new ApproveBlacklistServiceRequest(blacklistRequestId);
		blackListRequestService.approveBlacklistRequest(serviceRequest);
	}
}