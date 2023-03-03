package team.waitingcatch.app.lineup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class LineupController {
	@GetMapping("/lineup")
	public String getLineups() {
		return "seller/lineup-list";
	}

	@GetMapping("/templates/lineup-page")
	public String getLineupPage() {
		return "seller/lineup";
	}
}