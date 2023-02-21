package team.waitingcatch.app.restaurant.controller;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelRequestUserBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.RequestUserBlacklistByRestaurantControllerRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.RequestUserBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.service.requestblacklist.BlackListRequestService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@Controller
@RequiredArgsConstructor
public class BlacklistRequestController {
	private final BlackListRequestService blackListRequestService;

	@PostMapping("/blacklist")
	public void requestUserBlackListByRestaurant(
		@Valid @ModelAttribute RequestUserBlacklistByRestaurantControllerRequest controllerRequest,
		Model model,
		@AuthenticationPrincipal UserDetails userDetails) {

		var serviceRequest = new RequestUserBlacklistByRestaurantServiceRequest(controllerRequest.getUserId(),
			controllerRequest.getDescription(), userDetails.getUsername());
		blackListRequestService.requestUserBlackList(serviceRequest);
	}

	@PutMapping("/blacklist/{blacklistId}")
	public void cancelRequestUserBlackListByRestaurant(
		@PathVariable Long blacklistId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new CancelRequestUserBlackListByRestaurantServiceRequest(blacklistId,
			userDetails.getId());
		blackListRequestService.cancelRequestUserBlackList(serviceRequest);
	}
}