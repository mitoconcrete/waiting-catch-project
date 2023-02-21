package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlackListServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelRequestUserBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetRequestBlacklistResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.RequestUserBlacklistByRestaurantControllerRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.RequestUserBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.service.requestblacklist.BlackListRequestService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RequestMapping("/api")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlacklistRequestApiController {

	private final BlackListRequestService blackListRequestService;

	//판매자 권한 부분

	//판매자->관리자에게 고객 블랙리스트 추가 요청
	@PostMapping("/seller/blacklist/{userId}")
	public void requestUserBlackListByRestaurant(
		@RequestBody RequestUserBlacklistByRestaurantControllerRequest demandSignUpControllerRequest,
		@PathVariable Long userId,
		@AuthenticationPrincipal UserDetails userDetails) {

		RequestUserBlacklistByRestaurantServiceRequest requestUserBlackListByRestaurantServiceRequest
			= new RequestUserBlacklistByRestaurantServiceRequest(
			userId, demandSignUpControllerRequest.getDescription(), userDetails.getUsername());
		blackListRequestService.requestUserBlackList(requestUserBlackListByRestaurantServiceRequest);
	}

	@PutMapping("/blacklist/{blacklistId}")
	public void cancelRequestUserBlackListByRestaurant(
		@PathVariable Long blacklistId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		CancelRequestUserBlackListByRestaurantServiceRequest cancelRequestUserBlackListByRestaurantServiceRequest
			= new CancelRequestUserBlackListByRestaurantServiceRequest(blacklistId, userDetails.getId());
		blackListRequestService.cancelRequestUserBlackList(cancelRequestUserBlackListByRestaurantServiceRequest);
	}

	//관리자 부분
	//블랙리스트요청 전체조회
	@GetMapping("/admin/restaurants/blacklist-request")
	public List<GetRequestBlacklistResponse> getRequestBlackLists() {
		return blackListRequestService.getRequestBlackLists();
	}

	//블랙리스트요청 수락
	@PostMapping("/admin/restaurants/blacklist-requests/{blacklistRequestId}")
	public void approveBlackListRequest(@PathVariable Long blacklistRequestId) {
		ApproveBlackListServiceRequest approveBlackListServiceRequest = new ApproveBlackListServiceRequest(
			blacklistRequestId);
		blackListRequestService.approveBlackListRequest(approveBlackListServiceRequest);
	}

}