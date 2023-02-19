package team.waitingcatch.app.lineup.controller;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.lineup.dto.LineupRecordWithTypeResponse;
import team.waitingcatch.app.lineup.dto.StartLineupControllerRequest;
import team.waitingcatch.app.lineup.dto.StartLineupServiceRequest;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;
import team.waitingcatch.app.lineup.dto.UpdateArrivalStatusControllerRequest;
import team.waitingcatch.app.lineup.dto.UpdateArrivalStatusServiceRequest;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;
import team.waitingcatch.app.lineup.service.LineupService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
public class LineupController {
	private final LineupService lineupService;

	@PutMapping("/seller/start-lineup")
	public void openLineup(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		lineupService.openLineup(userDetails.getId());
	}

	@PutMapping("/seller/end-lineup")
	public void closeLineup(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		lineupService.closeLineup(userDetails.getId());
	}

	@PostMapping("/restaurants/{restaurantId}/waiting")
	public void lineup(
		@PathVariable Long restaurantId,
		@Valid @RequestBody StartLineupControllerRequest controllerRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		StartLineupServiceRequest serviceRequest = new StartLineupServiceRequest(userDetails.getUser(), restaurantId,
			controllerRequest.getNumOfMember(), LocalDateTime.now());
		lineupService.startWaiting(serviceRequest);
	}

	@GetMapping("/seller/lineup")
	public GenericResponse<TodayLineupResponse> getLineups(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return new GenericResponse<>(lineupService.getLineups(userDetails.getId()));
	}

	@GetMapping("/customer/past-lineup")
	public GenericResponse<LineupRecordWithTypeResponse> getPastLineups(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		return new GenericResponse<>(lineupService.getPastLineups(userDetails.getId()));
	}

	@PutMapping("/seller/lineup/{lineupId}/status")
	public void updateArrivalStatus(
		@PathVariable Long lineupId,
		@Valid @RequestBody UpdateArrivalStatusControllerRequest controllerRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		ArrivalStatusEnum status = ArrivalStatusEnum.valueOf(controllerRequest.getStatus());
		lineupService.updateArrivalStatus(new UpdateArrivalStatusServiceRequest(userDetails.getId(), lineupId, status));
	}
}