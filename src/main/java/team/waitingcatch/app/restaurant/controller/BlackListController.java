package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.DeleteUserBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListByRestaurantIdServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListResponse;
import team.waitingcatch.app.restaurant.service.blacklist.BlackListService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class BlackListController {

	private final BlackListService blackListService;

	//판매자 권한 부분
	//판매자 고객 블랙리스트 삭제
	@DeleteMapping("/seller/blacklist/{blacklistId}")
	public void deleteUserBlackListByRestaurant(@PathVariable Long blacklistId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		DeleteUserBlackListByRestaurantServiceRequest deleteUserBlackListByRestaurantServiceRequest
			= new DeleteUserBlackListByRestaurantServiceRequest(blacklistId, userDetails.getId());
		blackListService.deleteUserBlackListByRestaurant(deleteUserBlackListByRestaurantServiceRequest);
	}

	//레스토랑 별 블랙리스트 조회
	@GetMapping("/seller/restaurants/blacklist")
	public List<GetBlackListResponse> getBlackListByRestaurantId(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		GetBlackListByRestaurantIdServiceRequest getBlackListByRestaurantIdServiceRequest
			= new GetBlackListByRestaurantIdServiceRequest(userDetails.getId());
		return blackListService.getBlackListByRestaurantIdRequest(getBlackListByRestaurantIdServiceRequest);
	}
}

