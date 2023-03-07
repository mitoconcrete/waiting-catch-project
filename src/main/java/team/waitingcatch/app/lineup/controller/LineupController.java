package team.waitingcatch.app.lineup.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LineupController {
	@GetMapping("/seller/templates/lineup")
	public String getLineupPage(HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			System.out.println(token);
			model.addAttribute("accessToken", token);
		}
		return "seller/lineup";
	}
}