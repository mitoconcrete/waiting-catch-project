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
import team.waitingcatch.app.restaurant.dto.blacklist.GetRequestBlackListResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.RequestUserBlackListByRestaurantControllerRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.RequestUserBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.service.requestblacklist.BlackListRequestService;

@RequestMapping("/api")
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

	@PutMapping("/seller/blacklist/{userId}")
	public void cancelRequestUserBlackListByRestaurant(@PathVariable Long userId,
		@AuthenticationPrincipal UserDetails userDetails) {
		CancelRequestUserBlackListByRestaurantServiceRequest cancelRequestUserBlackListByRestaurantServiceRequest
			= new CancelRequestUserBlackListByRestaurantServiceRequest(userId, userDetails.getUsername());
		blackListRequestService.cancelRequestUserBlackList(cancelRequestUserBlackListByRestaurantServiceRequest);
	}

	//관리자 부분
	//블랙리스트요청 전체조회
	@GetMapping("/admin/restaurants/blacklist-request")
	public List<GetRequestBlackListResponse> getRequestBlackLists() {
		return blackListRequestService.getRequestBlackLists();
	}

	//블랙리스트요청 수락
	@PostMapping("/admin/restaurants/blacklist-request/{blacklistrequestId}")
	public void approveBlackListRequest(@PathVariable Long blacklistrequestId) {
		ApproveBlackListServiceRequest approveBlackListServiceRequest = new ApproveBlackListServiceRequest(
			blacklistrequestId);
		blackListRequestService.approveBlackListRequest(approveBlackListServiceRequest);
	}

}

