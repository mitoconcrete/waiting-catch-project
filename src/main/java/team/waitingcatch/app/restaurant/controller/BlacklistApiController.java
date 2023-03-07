package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.DeleteBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistByRestaurantIdServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistResponse;
import team.waitingcatch.app.restaurant.service.blacklist.BlacklistService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlacklistApiController {
	private final BlacklistService blacklistService;

	@DeleteMapping("/seller/blacklists/{blacklistId}")
	public void deleteBlacklist(
		@PathVariable Long blacklistId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new DeleteBlacklistByRestaurantServiceRequest(blacklistId, userDetails.getId());
		blacklistService.deleteBlacklistByRestaurant(serviceRequest);
	}

	@GetMapping("/admin/restaurants/{restaurantId}/blacklists")
	public GenericResponse<GetBlacklistResponse> getBlacklistsByRestaurant(@PathVariable Long restaurantId) {
		var serviceRequest = new GetBlacklistByRestaurantIdServiceRequest(restaurantId);
		return new GenericResponse(blacklistService.getBlackListByRestaurantId(serviceRequest));
	}

	@GetMapping("/admin/blacklists")
	public GenericResponse<Page<GetBlacklistResponse>> getBlacklist(
		@PageableDefault Pageable pageable) {
		return new GenericResponse<>(blacklistService.getBlacklist(pageable));
	}

	@GetMapping("/seller/restaurant/blacklist")
	public List<GetBlacklistResponse> getBlackListByRestaurant(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		GetBlackListByRestaurantServiceRequest getBlackListByRestaurantServiceRequest = new
			GetBlackListByRestaurantServiceRequest(userDetails.getId());
		return blacklistService.getBlackListByRestaurant(getBlackListByRestaurantServiceRequest);
	}
}