package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.DeleteBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistByRestaurantIdServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistResponse;
import team.waitingcatch.app.restaurant.service.blacklist.BlacklistService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@Controller
@RequiredArgsConstructor
public class BlacklistController {
	private final BlacklistService blackListService;

	@DeleteMapping("/blacklists/{blacklistId}")
	public void deleteUserBlackListByRestaurant(
		@PathVariable Long blacklistId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new DeleteBlacklistByRestaurantServiceRequest(blacklistId, userDetails.getId());
		blackListService.deleteBlacklistByRestaurant(serviceRequest);
	}

	@GetMapping("/admin/restaurants/{restaurantId}/blacklist")
	public GenericResponse<GetBlacklistResponse> getBlacklistByRestaurantId(@PathVariable Long restaurantId) {

		var serviceRequest = new GetBlacklistByRestaurantIdServiceRequest(restaurantId);
		List<GetBlacklistResponse> blacklistResponses = blackListService.getBlacklistByRestaurantIdRequest(
			serviceRequest);
		return new GenericResponse<>(blacklistResponses);
	}
}