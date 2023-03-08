package team.waitingcatch.app.restaurant.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistDemandResponse;
import team.waitingcatch.app.restaurant.service.blacklistdemand.BlacklistDemandService;

@Controller
@RequiredArgsConstructor
public class BlacklistDemandController {
	private final BlacklistDemandService blacklistDemandService;

	@GetMapping("/seller/templates/blacklist-demands")
	public String getBlacklistDemandPage(HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		return "seller/blacklist-demand";
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
}