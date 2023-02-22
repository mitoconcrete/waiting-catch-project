package team.waitingcatch.app.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.service.event.EventService;
import team.waitingcatch.app.restaurant.service.requestblacklist.BlackListRequestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
	private final EventService eventService;
	private final BlackListRequestService blackListRequestService;

	@GetMapping("/login-page")
	public ModelAndView loginPage() {
		return new ModelAndView("/admin/login");
	}

	@GetMapping("/main")
	public ModelAndView adminMainPage() {
		return new ModelAndView("/admin/index");
	}

	@GetMapping("/seller-management")
	public ModelAndView sellerManagementPage() {
		return new ModelAndView("/admin/seller-management");
	}

	@GetMapping("/user-list")
	public ModelAndView userListPage() {
		return new ModelAndView("/admin/user-list");
	}

	@GetMapping("/restaurant-list")
	public ModelAndView restaurantListPage() {
		return new ModelAndView("/admin/restaurant-list");
	}

	@GetMapping("/event")
	public ModelAndView eventPage(Model model) {
		model.addAttribute("event", eventService.getGlobalEvents());
		return new ModelAndView("/admin/event");
	}

	// @GetMapping("/blacklist")
	// public ModelAndView blacklistPage(Model model) {
	// 	model.addAttribute("blacklist", blackListRequestService.getRequestBlackLists());
	// 	return new ModelAndView("/admin/blacklist");
	// }

	@GetMapping("/review")
	public ModelAndView censorReview() {
		return new ModelAndView("/admin/review");
	}

	@GetMapping("/category")
	public ModelAndView categoryPage() {
		return new ModelAndView("/admin/category");
	}
}

