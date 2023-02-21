package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlackListServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.DemandBlacklistByRestaurantControllerRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.DemandBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetRequestBlacklistResponse;
import team.waitingcatch.app.restaurant.service.requestblacklist.BlacklistRequestService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlacklistRequestApiController {
	private final BlacklistRequestService blackListRequestService;

	//판매자 권한 부분

	//판매자->관리자에게 고객 블랙리스트 추가 요청
	@PostMapping("/blacklists")
	public void requestUserBlackListByRestaurant(
		@Valid @RequestBody DemandBlacklistByRestaurantControllerRequest controllerRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new DemandBlacklistByRestaurantServiceRequest(userDetails.getId(),
			controllerRequest.getUserId(), controllerRequest.getDescription());
		blackListRequestService.requestUserBlackList(serviceRequest);
	}

	@PutMapping("/blacklists/{blacklistId}")
	public void cancelRequestUserBlackListByRestaurant(
		@PathVariable Long blacklistId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new CancelBlacklistByRestaurantServiceRequest(blacklistId, userDetails.getId());
		blackListRequestService.cancelRequestUserBlacklist(serviceRequest);
	}

	//관리자 부분
	//블랙리스트요청 전체조회
	@GetMapping("/admin/restaurants/blacklist-request")
	public List<GetRequestBlacklistResponse> getRequestBlacks() {
		return blackListRequestService.getRequestBlacklist();
	}

	//블랙리스트요청 수락
	@PostMapping("/admin/restaurants/blacklist-request/{blacklistRequestId}")
	public void approveBlackListRequest(@PathVariable Long blacklistRequestId) {
		var serviceRequest = new ApproveBlackListServiceRequest(blacklistRequestId);
		blackListRequestService.approveBlacklistRequest(serviceRequest);
	}
}