package team.waitingcatch.app.lineup.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.lineup.dto.CancelWaitingRequest;
import team.waitingcatch.app.lineup.dto.GetLineupHistoryRecordsServiceRequest;
import team.waitingcatch.app.lineup.dto.GetLineupRecordsServiceRequest;
import team.waitingcatch.app.lineup.dto.LineupRecordWithTypeResponse;
import team.waitingcatch.app.lineup.dto.StartLineupControllerRequest;
import team.waitingcatch.app.lineup.dto.StartWaitingServiceRequest;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;
import team.waitingcatch.app.lineup.dto.UpdateArrivalStatusControllerRequest;
import team.waitingcatch.app.lineup.dto.UpdateArrivalStatusServiceRequest;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;
import team.waitingcatch.app.lineup.service.LineupService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RestController
@RequestMapping("/api")
@Validated
@RequiredArgsConstructor
public class LineupApiController {
	private final LineupService lineupService;

	@PutMapping("/seller/open-lineup")
	public void openLineup(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		lineupService.openLineup(userDetails.getId());
	}

	@PutMapping("/seller/close-lineup")
	public void closeLineup(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		lineupService.closeLineup(userDetails.getId());
	}

	@PostMapping("/restaurants/{restaurantId}/lineup")
	@ResponseStatus(HttpStatus.CREATED)
	public void startWaiting(
		@PathVariable long restaurantId,
		@Valid @RequestBody StartLineupControllerRequest controllerRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new StartWaitingServiceRequest(userDetails.getUser(), restaurantId,
			controllerRequest.getLatitude(), controllerRequest.getLongitude(), controllerRequest.getNumOfMember(),
			LocalDateTime.now());
		lineupService.startWaiting(serviceRequest);
	}

	@DeleteMapping("/restaurants/lineup/{lineupId}")
	public void cancelWaiting(@PathVariable long lineupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		lineupService.cancelWaiting(new CancelWaitingRequest(lineupId, userDetails.getId()));
	}

	@GetMapping("/seller/lineup")
	public GenericResponse<List<TodayLineupResponse>> getLineups(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return new GenericResponse<>(lineupService.getTodayLineups(userDetails.getId()));
	}

	@GetMapping("/customer/lineup-records")
	public GenericResponse<List<LineupRecordWithTypeResponse>> getLineupRecords(
		@RequestParam(required = false) @Pattern(regexp = "^(WAIT|CALL|CANCEL|ARRIVE)$") String status,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var serviceRequest = new GetLineupRecordsServiceRequest(userDetails.getId(),
			status != null ? ArrivalStatusEnum.valueOf(status) : null);

		return new GenericResponse<>(lineupService.getLineupRecords(serviceRequest));
	}

	@GetMapping("/customer/lineup-history-records")
	public GenericResponse<Slice<LineupRecordWithTypeResponse>> getLineupRecords(
		@RequestParam(required = false) Long lastId,
		@RequestParam(required = false) @Pattern(regexp = "^(WAIT|CALL|CANCEL|ARRIVE)$") String status,
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PageableDefault Pageable pageable) {

		var serviceRequest = new GetLineupHistoryRecordsServiceRequest(lastId, userDetails.getId(),
			status != null ? ArrivalStatusEnum.valueOf(status) : null);

		return new GenericResponse<>(lineupService.getLineupHistoryRecords(serviceRequest, pageable));
	}

	@PutMapping("/seller/lineup/{lineupId}/status")
	public void updateArrivalStatus(
		@PathVariable long lineupId,
		@Valid @RequestBody UpdateArrivalStatusControllerRequest controllerRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		var status = ArrivalStatusEnum.valueOf(controllerRequest.getStatus());
		lineupService.updateArrivalStatus(new UpdateArrivalStatusServiceRequest(userDetails.getId(), lineupId, status));
	}
}