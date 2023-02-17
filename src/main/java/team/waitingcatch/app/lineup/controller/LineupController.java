package team.waitingcatch.app.lineup.controller;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.lineup.dto.PastLineupResponse;
import team.waitingcatch.app.lineup.dto.StartLineupControllerRequest;
import team.waitingcatch.app.lineup.dto.StartLineupServiceRequest;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;
import team.waitingcatch.app.lineup.service.LineupService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
public class LineupController {
	private final LineupService lineupService;

	@PostMapping("/restaurants/{restaurantId}/waiting")
	public void lineup(@PathVariable Long restaurantId,
		@Valid @RequestBody StartLineupControllerRequest controllerRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		StartLineupServiceRequest serviceRequest = new StartLineupServiceRequest(userDetails.getUser(), restaurantId,
			controllerRequest.getNumOfMember(), LocalDateTime.now());
		lineupService.startLineup(serviceRequest);
	}

	@GetMapping("/seller/lineup")
	public GenericResponse<TodayLineupResponse> getLineups(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return new GenericResponse(lineupService.getLineups(userDetails.getId()));
	}

	@GetMapping("/customer/past-lineup")
	public GenericResponse<PastLineupResponse> getPastLineups(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return new GenericResponse(lineupService.getPastLineups(userDetails.getId()));
	}
}