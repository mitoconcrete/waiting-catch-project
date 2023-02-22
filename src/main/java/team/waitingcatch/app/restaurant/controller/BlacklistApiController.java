package team.waitingcatch.app.restaurant.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.DeleteBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistByRestaurantIdServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistResponse;
import team.waitingcatch.app.restaurant.service.blacklist.BlacklistService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlacklistApiController {
	private final BlacklistService blackListService;

	//판매자 권한 부분
	//판매자 고객 블랙리스트 삭제
	@DeleteMapping("/blacklists/{blacklistId}")
	public void deleteUserBlackListByRestaurant(
		@PathVariable Long blacklistId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new DeleteBlacklistByRestaurantServiceRequest(blacklistId, userDetails.getId());
		blackListService.deleteBlacklistByRestaurant(serviceRequest);
	}

	//레스토랑 별 블랙리스트 조회
	@GetMapping("/admin/restaurants/{restaurantId}/blacklist")
	public GenericResponse<GetBlacklistResponse> getBlacklistByRestaurantId(@PathVariable Long restaurantId) {
		var serviceRequest = new GetBlacklistByRestaurantIdServiceRequest(restaurantId);
		return new GenericResponse<>(blackListService.getBlacklistByRestaurantIdRequest(serviceRequest));
	}
}