package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.BlacklistDemandControllerRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListDemandByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistDemandResponse;
import team.waitingcatch.app.restaurant.service.blacklistdemand.BlacklistDemandService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlacklistDemandApiController {
	private final BlacklistDemandService blacklistDemandService;

	@GetMapping("/seller/blacklist-demands")
	public GenericResponse<List<GetBlacklistDemandResponse>> getBlackListDemandByRestaurant(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new GetBlackListDemandByRestaurantServiceRequest(userDetails.getId());
		return new GenericResponse<>(blacklistDemandService.getBlackListDemandsByRestaurant(serviceRequest));
	}

	@PostMapping("/seller/blacklist-demands")
	public void createBlacklistDemand(
		@Valid @RequestBody BlacklistDemandControllerRequest controllerRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new CreateBlacklistDemandServiceRequest(userDetails.getId(), controllerRequest.getUserId(),
			controllerRequest.getDescription());
		blacklistDemandService.submitBlacklistDemand(serviceRequest);
	}

	@DeleteMapping("/seller/blacklist-demands/{blacklistDemandId}")
	public void cancelBlacklistDemand(
		@PathVariable Long blacklistDemandId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new CancelBlacklistDemandServiceRequest(blacklistDemandId, userDetails.getId());
		blacklistDemandService.cancelBlacklistDemand(serviceRequest);
	}

	@GetMapping("/admin/restaurants/blacklist-demands")
	public GenericResponse<Page<GetBlacklistDemandResponse>> getBlacklistDemands(
		@PageableDefault Pageable pageable) {
		return new GenericResponse<>(blacklistDemandService.getBlacklistDemands(pageable));
	}

	@PostMapping("/admin/restaurants/blacklist-demands/{blacklistDemandId}")
	public void approveBlacklistDemand(@PathVariable Long blacklistDemandId) {
		var serviceRequest = new ApproveBlacklistDemandServiceRequest(blacklistDemandId);
		blacklistDemandService.approveBlacklistDemand(serviceRequest);
	}

	@PutMapping("/admin/restaurants/blacklist-demand/{blacklistDemandId}")
	public void rejectBlacklistDemand(@PathVariable Long blacklistDemandId) {
		blacklistDemandService.rejectBlacklistDemand(blacklistDemandId);
	}
}