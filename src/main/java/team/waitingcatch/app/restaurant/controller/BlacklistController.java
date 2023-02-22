package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.DeleteBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistByRestaurantIdServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistResponse;
import team.waitingcatch.app.restaurant.service.blacklist.BlacklistService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@Controller
@RequiredArgsConstructor
public class BlacklistController {
	private final BlacklistService blacklistService;

	@DeleteMapping("/blacklists/{blacklistId}")
	public void deleteBlacklist(
		@PathVariable Long blacklistId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new DeleteBlacklistByRestaurantServiceRequest(blacklistId, userDetails.getId());
		blacklistService.deleteBlacklistByRestaurant(serviceRequest);
	}

	@GetMapping("/admin/restaurants/{restaurantId}/blacklists")
	public List<GetBlacklistResponse> getBlacklistsByRestaurant(@PathVariable Long restaurantId) {
		var serviceRequest = new GetBlacklistByRestaurantIdServiceRequest(restaurantId);
		return blacklistService.getBlackListByRestaurantId(serviceRequest);
	}

	@GetMapping("/seller/restaurants/blacklist")
	public List<GetBlacklistResponse> getBlackListByRestaurant(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		GetBlackListByRestaurantServiceRequest getBlackListByRestaurantServiceRequest = new
			GetBlackListByRestaurantServiceRequest(userDetails.getId());
		return blacklistService.getBlackListByRestaurant(getBlackListByRestaurantServiceRequest);
	}
}