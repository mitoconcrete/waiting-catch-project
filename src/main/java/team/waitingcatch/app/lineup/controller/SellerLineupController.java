package team.waitingcatch.app.lineup.controller;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.dto.UpdateArrivalStatusControllerRequest;
import team.waitingcatch.app.lineup.dto.UpdateArrivalStatusServiceRequest;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;
import team.waitingcatch.app.lineup.service.LineupService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerLineupController {
	private final LineupService lineupService;

	@PutMapping("/open-lineup")
	@ResponseBody
	public void openLineup(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		lineupService.openLineup(userDetails.getId());
	}

	@PutMapping("/close-lineup")
	@ResponseBody
	public void closeLineup(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		lineupService.closeLineup(userDetails.getId());
	}

	@GetMapping("/lineup")
	public String getLineups(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		model.addAttribute("lineupList", lineupService.getTodayLineups(userDetails.getId()));
		return "seller/lineup-list";
	}

	@GetMapping("/lineup-page")
	public String getLineupPage() {
		return "seller/lineup";
	}

	@PutMapping("/lineup/{lineupId}/status")
	@ResponseBody
	public void updateArrivalStatus(
		@PathVariable long lineupId,
		@Valid @ModelAttribute UpdateArrivalStatusControllerRequest controllerRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		ArrivalStatusEnum status = ArrivalStatusEnum.valueOf(controllerRequest.getStatus());
		lineupService.updateArrivalStatus(new UpdateArrivalStatusServiceRequest(userDetails.getId(), lineupId, status));
	}
}