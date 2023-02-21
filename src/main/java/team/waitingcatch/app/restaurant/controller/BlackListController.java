package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.DeleteUserBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListByRestaurantIdServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListResponse;
import team.waitingcatch.app.restaurant.service.blacklist.BlackListService;

@RestController
@RequiredArgsConstructor
public class BlackListController {

	private final BlackListService blackListService;

	//판매자 권한 부분
	//판매자 고객 블랙리스트 삭제
	@DeleteMapping("/seller/blacklist/{userId}")
	public void deleteUserBlackListByRestaurant(@PathVariable Long userId,
		@AuthenticationPrincipal UserDetails userDetails) {
		DeleteUserBlackListByRestaurantServiceRequest deleteUserBlackListByRestaurantServiceRequest
			= new DeleteUserBlackListByRestaurantServiceRequest(userId, userDetails.getUsername());
		blackListService.deleteUserBlackListByRestaurant(deleteUserBlackListByRestaurantServiceRequest);
	}

	//레스토랑 별 블랙리스트 조회
	@GetMapping("/admin/restaurants/{restaurantId}/blacklist")
	public List<GetBlackListResponse> getBlackListByRestaurantId(@PathVariable Long restaurantId) {
		GetBlackListByRestaurantIdServiceRequest getBlackListByRestaurantIdServiceRequest
			= new GetBlackListByRestaurantIdServiceRequest(restaurantId);
		return blackListService.getBlackListByRestaurantIdRequest(getBlackListByRestaurantIdServiceRequest);
	}

	// 전체 블랙리스트 조회
	@GetMapping("/admin/blacklist")
	public List<GetBlackListResponse> getBlacklist() {
		return blackListService.getBlacklist();
	}
}

