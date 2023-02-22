package team.waitingcatch.app.restaurant.controller;

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
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlackListServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelBlacklistRequestServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.DemandBlacklistRequestControllerRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.GetRequestBlacklistResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.BlacklistRequestServiceRequest;
import team.waitingcatch.app.restaurant.service.requestblacklist.BlacklistRequestService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlacklistRequestApiController {
	private final BlacklistRequestService blackListRequestService;

	//판매자 권한 부분

	//판매자->관리자에게 고객 블랙리스트 추가 요청
	@PostMapping("/blacklist-requests")
	public void demandBlackListRequest(
		@Valid @RequestBody DemandBlacklistRequestControllerRequest controllerRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new BlacklistRequestServiceRequest(userDetails.getId(),
			controllerRequest.getUserId(), controllerRequest.getDescription());
		blackListRequestService.requestUserBlacklist(serviceRequest);
	}

	@PutMapping("/blacklist-requests/{blacklistRequestId}")
	public void cancelBlackListRequest(
		@PathVariable Long blacklistRequestId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new CancelBlacklistRequestServiceRequest(blacklistRequestId, userDetails.getId());
		blackListRequestService.cancelRequestUserBlacklist(serviceRequest);
	}

	//관리자 부분
	//블랙리스트요청 전체조회
	@GetMapping("/admin/restaurants/blacklist-requests")
	public GenericResponse<GetBlacklistResponse> getRequestBlacks() {
		return new GenericResponse<>(blackListRequestService.getRequestBlacklists());
	}

	//블랙리스트요청 수락
	@PostMapping("/admin/restaurants/blacklist-requests/{blacklistRequestId}")
	public void approveBlackListRequest(@PathVariable Long blacklistRequestId) {
		var serviceRequest = new ApproveBlackListServiceRequest(blacklistRequestId);
		blackListRequestService.approveBlacklistRequest(serviceRequest);
	}
}