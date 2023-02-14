package team.waitingcatch.app.restaurant.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.RequestUserBlackListByRestaurantControllerRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.RequestUserBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.service.requestblacklist.BlackListRequestService;

@RestController
@RequiredArgsConstructor
public class RequestBlackListController {

	private final BlackListRequestService blackListRequestService;

	//판매자 권한 부분

	//판매자->관리자에게 고객 블랙리스트 추가 요청
	@PostMapping("/seller/blacklist/{userId}")
	public void requestUserBlackListByRestaurant(
		@RequestBody RequestUserBlackListByRestaurantControllerRequest demandSignUpControllerRequest,
		@PathVariable Long userId,
		@AuthenticationPrincipal UserDetails userDetails) {

		RequestUserBlackListByRestaurantServiceRequest requestUserBlackListByRestaurantServiceRequest
			= new RequestUserBlackListByRestaurantServiceRequest(
			demandSignUpControllerRequest.getDescription(), userId, userDetails.getUsername());
		blackListRequestService.requestUserBlackList(requestUserBlackListByRestaurantServiceRequest);
	}

}

