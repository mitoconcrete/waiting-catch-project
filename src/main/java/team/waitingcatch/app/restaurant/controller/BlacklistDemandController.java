package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistDemandResponse;
import team.waitingcatch.app.restaurant.service.requestblacklist.BlacklistDemandService;

@Controller
@RequiredArgsConstructor
public class BlacklistDemandController {
	private final BlacklistDemandService blacklistDemandService;

	@GetMapping("/seller/templates/blacklist-demand-page")
	public String getBlacklistDemandPage() {
		return "seller/blacklist-demand";
	}

	@GetMapping("/seller/blacklist-demands")
	public String getBlackListDemandByRestaurant() {
		return "seller/blacklist-demand-list";
	}

	@GetMapping("/admin/restaurants/blacklist-demands")
	public List<GetBlacklistDemandResponse> getBlacklistDemands() {
		return blacklistDemandService.getBlacklistDemands();
	}

	@PostMapping("/admin/restaurants/blacklist-demands/{blacklistDemandId}")
	public void approveBlacklistDemand(@PathVariable Long blacklistDemandId) {
		var serviceRequest = new ApproveBlacklistDemandServiceRequest(blacklistDemandId);
		blacklistDemandService.approveBlacklistDemand(serviceRequest);
	}
}