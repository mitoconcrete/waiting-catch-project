package team.waitingcatch.app.restaurant.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.common.util.JwtUtil;
import team.waitingcatch.app.event.dto.couponcreator.CreateCouponCreatorControllerRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateCouponCreatorControllerRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.CreateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.DeleteEventServiceRequest;
import team.waitingcatch.app.event.dto.event.GetEventsResponse;
import team.waitingcatch.app.event.dto.event.UpdateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.UpdateSellerEventServiceRequest;
import team.waitingcatch.app.event.service.couponcreator.CouponCreatorService;
import team.waitingcatch.app.event.service.event.EventService;
import team.waitingcatch.app.restaurant.dto.menu.CreateMenuControllerRequest;
import team.waitingcatch.app.restaurant.dto.menu.CreateMenuServiceRequest;
import team.waitingcatch.app.restaurant.dto.menu.DeleteMenuServiceRequest;
import team.waitingcatch.app.restaurant.dto.menu.MenuResponse;
import team.waitingcatch.app.restaurant.dto.menu.UpdateMenuControllerRequest;
import team.waitingcatch.app.restaurant.dto.menu.UpdateMenuServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerControllerRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantControllerRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.service.menu.MenuService;
import team.waitingcatch.app.restaurant.service.requestseller.SellerManagementService;
import team.waitingcatch.app.restaurant.service.restaurant.MapApiService;
import team.waitingcatch.app.restaurant.service.restaurant.RestaurantService;
import team.waitingcatch.app.user.dto.DeleteUserRequest;
import team.waitingcatch.app.user.dto.LoginRequest;
import team.waitingcatch.app.user.dto.LoginServiceResponse;
import team.waitingcatch.app.user.dto.LogoutRequest;
import team.waitingcatch.app.user.dto.UpdateUserControllerRequest;
import team.waitingcatch.app.user.dto.UpdateUserServiceRequest;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;
import team.waitingcatch.app.user.service.UserService;

@Controller
@RequiredArgsConstructor
public class SellerController {
	private final SellerManagementService sellerManagementService;
	private final JwtUtil jwtUtil;
	private final UserService userService;
	private final MenuService menuService;
	private final EventService eventService;
	private final CouponCreatorService couponCreatorService;
	private final RestaurantService restaurantService;
	private final MapApiService mapApiService;
	/*     로그인 프론트     */

	@GetMapping("/")
	public String index() {
		return "seller/lineup";
	}

	@GetMapping("/general/templates/seller/login")
	public String login() {
		return "seller/login";
	}

	@ResponseBody
	@PostMapping("/api/general/seller/login")
	public String login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		LoginServiceResponse loginServiceResponse = userService.login(loginRequest);
		// 엑세스 토큰을 서비스로 부터 반환 받아 헤더에 넣어준다.
		response.setHeader(JwtUtil.AUTHORIZATION_HEADER, loginServiceResponse.getAccessToken());
		return "success";
	}

	@GetMapping("/general/templates/seller/signup")
	public String signup() {
		return "seller/register";
	}

	@PostMapping("/api/general/seller/signup")
	public String demandSignUpSeller(
		@Valid DemandSignUpSellerControllerRequest demandSignUpControllerRequest) {
		Position position = mapApiService.getPosition(demandSignUpControllerRequest.getQuery());

		DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest = new DemandSignUpSellerServiceRequest(
			demandSignUpControllerRequest, position);
		sellerManagementService.demandSignUpSeller(demandSignupSellerServiceRequest);
		return "seller/login";
	}

	/*     메뉴 프론트     */

	@GetMapping("/seller/templates/menu")
	public String menu(Model model,
		@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		List<MenuResponse> menus = menuService.getMyRestaurantMenus(userDetails.getId());
		model.addAttribute("menus", menus);
		return "seller/menu";
	}

	@GetMapping("/seller/templates/menu/new")
	public String createMenu(Model model, HttpServletResponse response) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		return "seller/menu-new";
	}

	//@PostMapping("/seller/restaurants/{restaurantId}/menus")
	@PostMapping("/api/seller/menu/new")
	public String createMenu(
		@RequestPart(value = "image", required = false) MultipartFile multipartFile,
		@Valid CreateMenuControllerRequest request,
		@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response,
		Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			Cookie cookie = new Cookie("Authorization",
				URLEncoder.encode(token, StandardCharsets.UTF_8));
			cookie.setSecure(false);
			cookie.setMaxAge(7 * 24 * 60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		CreateMenuServiceRequest serviceRequest = new CreateMenuServiceRequest(userDetails.getId(), multipartFile,
			request);
		menuService.createMenu(serviceRequest);
		model.addAttribute("message", "메뉴 등록에 성공하였습니다.");
		model.addAttribute("searchUrl", "/seller/templates/menu");
		return "/admin/message";
	}

	@GetMapping("/seller/templates/menus/{menuId}/menu-form")
	public String updateMenu(Model model, @PathVariable Long menuId, HttpServletResponse response) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		model.addAttribute("menuId", menuId);
		return "seller/menu-update";
	}

	@PutMapping("/api/seller/menus/{menuId}/menu-form")
	public String updateMenu(@PathVariable Long menuId,
		@RequestPart(value = "image", required = false) MultipartFile multipartFile,
		@Valid UpdateMenuControllerRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails,
		Model model, HttpServletResponse response
	) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			Cookie cookie = new Cookie("Authorization",
				URLEncoder.encode(token, StandardCharsets.UTF_8));
			cookie.setSecure(false);
			cookie.setMaxAge(7 * 24 * 60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		UpdateMenuServiceRequest serviceRequest = new UpdateMenuServiceRequest(menuId, request, multipartFile,
			userDetails.getId());
		menuService.updateMenu(serviceRequest);
		model.addAttribute("message", "메뉴 수정에 성공하였습니다.");
		model.addAttribute("searchUrl", "/seller/templates/menu");
		return "/admin/message";
	}

	@GetMapping("/seller/templates/menus/{menuId}")
	public String deleteMenuSub(@PathVariable Long menuId, @AuthenticationPrincipal UserDetailsImpl userDetails,
		HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			Cookie cookie = new Cookie("Authorization",
				URLEncoder.encode(token, StandardCharsets.UTF_8));
			cookie.setSecure(false);
			cookie.setMaxAge(7 * 24 * 60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		deleteMenu(menuId, userDetails);
		model.addAttribute("message", "메뉴 삭제에 성공하였습니다.");
		model.addAttribute("searchUrl", "/seller/templates/menu");
		return "/admin/message";
	}

	@DeleteMapping("/api/seller/menus/{menuId}")
	public void deleteMenu(@PathVariable Long menuId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		DeleteMenuServiceRequest deleteMenuServiceRequest = new DeleteMenuServiceRequest(userDetails.getId(), menuId);
		menuService.deleteMenu(deleteMenuServiceRequest);
	}

	/*     판매자 정보 프론트   */
	@GetMapping("/seller/templates/seller")
<<<<<<< HEAD
	public String seller() {
		return "seller/seller";
=======
	public String seller(HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		return "/seller/seller";
>>>>>>> c9d22ae50d04f351b59fe65647d2117da5cfc076
	}

	@GetMapping("/api/seller/logout")
	public String logoutSub(HttpServletRequest request, HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			Cookie cookie = new Cookie("Authorization",
				URLEncoder.encode(token, StandardCharsets.UTF_8));
			cookie.setSecure(false);
			cookie.setMaxAge(7 * 24 * 60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		String token = jwtUtil.resolveToken(request);
		LogoutRequest servicePayload = new LogoutRequest(token);
		userService.logout(servicePayload);
		model.addAttribute("message", "로그아웃에 성공했습니다.");
		model.addAttribute("searchUrl", "/general/templates/seller/login");
		return "/admin/message";
	}

	@DeleteMapping("/seller/templates/withdraw")
	public String withdrawSellerSub(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response,
		Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			Cookie cookie = new Cookie("Authorization",
				URLEncoder.encode(token, StandardCharsets.UTF_8));
			cookie.setSecure(false);
			cookie.setMaxAge(7 * 24 * 60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		withdrawSeller(userDetails);
		model.addAttribute("message", "회원탈퇴에 성공했습니다.");
		model.addAttribute("searchUrl", "/general/templates/seller/login");
		return "/admin/message";
	}

	@DeleteMapping("/api/seller/withdraws")
	public void withdrawSeller(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		DeleteUserRequest servicePayload = new DeleteUserRequest(userDetails.getUsername());
		userService.deleteSeller(servicePayload);
	}

	@GetMapping("/seller/templates/info")
<<<<<<< HEAD
	public String updateSellerInfoSub() {
		return "seller/seller-info";
=======
	public String updateSellerInfoSub(HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		return "/seller/seller-info";
>>>>>>> c9d22ae50d04f351b59fe65647d2117da5cfc076
	}

	@PutMapping("/api/seller/info/view")
	public String updateSellerInfo(@Valid UpdateUserControllerRequest controllerRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			Cookie cookie = new Cookie("Authorization",
				URLEncoder.encode(token, StandardCharsets.UTF_8));
			cookie.setSecure(false);
			cookie.setMaxAge(7 * 24 * 60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		UpdateUserServiceRequest servicePayload = new UpdateUserServiceRequest(controllerRequest.getName(),
			controllerRequest.getEmail(), userDetails.getUsername(), controllerRequest.getNickName(),
			controllerRequest.getPhoneNumber());
		userService.updateUser(servicePayload);
		model.addAttribute("message", "정보 수정에 성공했습니다.");
		model.addAttribute("searchUrl", "/seller/templates/seller");
		return "/admin/message";
	}

	@GetMapping("/seller/templates/update-restaurant")
<<<<<<< HEAD
	public String updateRestaurantSub() {
		return "seller/seller-restaurant-update";
=======
	public String updateRestaurantSub(HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		return "/seller/seller-restaurant-update";
>>>>>>> c9d22ae50d04f351b59fe65647d2117da5cfc076
	}

	@PutMapping(value = "/api/seller/update-restaurant")
	public String updateRestaurant(
		UpdateRestaurantControllerRequest updateRestaurantControllerRequest,
		@RequestPart(value = "image", required = false) List<MultipartFile> multipartFile,
		@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response, Model model) throws
		IOException {

		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			Cookie cookie = new Cookie("Authorization",
				URLEncoder.encode(token, StandardCharsets.UTF_8));
			cookie.setSecure(false);
			cookie.setMaxAge(7 * 24 * 60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);
		}

		UpdateRestaurantServiceRequest updateRestaurantServiceRequest =
			new UpdateRestaurantServiceRequest(updateRestaurantControllerRequest, multipartFile,
				userDetails.getId());
		restaurantService.updateRestaurant(updateRestaurantServiceRequest);
		model.addAttribute("message", "정보 수정에 성공했습니다.");
		model.addAttribute("searchUrl", "/seller/templates/seller");
		return "/admin/message";
	}

	/*     이벤트     */

	@GetMapping("/seller/templates/event")
	public String event(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails,
		@PageableDefault(size = 10, page = 0) Pageable pageable, HttpServletResponse response) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		Page<GetEventsResponse> events = eventService.getRestaurantEvents(userDetails.getId(), pageable);
		model.addAttribute("events", events);
		return "seller/event";
	}

	@GetMapping("/seller/templates/event/creator")
<<<<<<< HEAD
	public String createEvent() {
		return "seller/event-create";
=======
	public String createEvent(HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		return "/seller/event-create";
>>>>>>> c9d22ae50d04f351b59fe65647d2117da5cfc076
	}

	@PostMapping("/api/seller/event")
	public String createEvent(
		@Validated CreateEventControllerRequest createEventControllerRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			Cookie cookie = new Cookie("Authorization",
				URLEncoder.encode(token, StandardCharsets.UTF_8));
			cookie.setSecure(false);
			cookie.setMaxAge(7 * 24 * 60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		CreateEventServiceRequest createEventServiceRequest = new CreateEventServiceRequest(
			createEventControllerRequest, userDetails.getId());
		eventService.createSellerEvent(createEventServiceRequest);
		model.addAttribute("message", "이벤트 생성에 성공했습니다.");
		model.addAttribute("searchUrl", "/seller/templates/event");
		return "/admin/message";
	}

	@GetMapping("/seller/templates/events/{eventId}/coupon-creators")
	public String createCouponCreator(Model model, @PathVariable Long eventId, HttpServletResponse response) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		model.addAttribute("eventId", eventId);
		return "seller/event-create-creator";
	}

	@PostMapping("/api/seller/events/{eventId}/coupon-creators")
	public String createCouponCreator(@PathVariable Long eventId,
		@Validated CreateCouponCreatorControllerRequest createCouponCreatorControllerRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			Cookie cookie = new Cookie("Authorization",
				URLEncoder.encode(token, StandardCharsets.UTF_8));
			cookie.setSecure(false);
			cookie.setMaxAge(7 * 24 * 60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		CreateSellerCouponCreatorServiceRequest createSellerCouponCreatorServiceRequest = new CreateSellerCouponCreatorServiceRequest(
			createCouponCreatorControllerRequest, eventId, userDetails.getId());

		couponCreatorService.createSellerCouponCreator(createSellerCouponCreatorServiceRequest);
		model.addAttribute("message", "쿠폰 생성자 생성에 성공했습니다.");
		model.addAttribute("searchUrl", "/seller/templates/event");
		return "/admin/message";
	}

	@GetMapping("/seller/templates/events/{eventId}/update")
	public String updateEvent(Model model, @PathVariable Long eventId, HttpServletResponse response) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		model.addAttribute("eventId", eventId);
		return "seller/event-update";
	}

	@PutMapping("/api/seller/events/{eventId}/update")
	public String updateEvent(UpdateEventControllerRequest updateEventControllerRequest,
		@PathVariable Long eventId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response,
		Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			Cookie cookie = new Cookie("Authorization",
				URLEncoder.encode(token, StandardCharsets.UTF_8));
			cookie.setSecure(false);
			cookie.setMaxAge(7 * 24 * 60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		UpdateSellerEventServiceRequest updateSellerEventServiceRequest = new UpdateSellerEventServiceRequest(
			updateEventControllerRequest, eventId, userDetails.getId());
		eventService.updateSellerEvent(updateSellerEventServiceRequest);
		model.addAttribute("message", "이벤트 수정에 성공했습니다.");
		model.addAttribute("searchUrl", "/seller/templates/event");
		return "/admin/message";
	}

	@GetMapping("/seller/templates/events/{eventId}/coupon-creators/{creatorId}")
	public String updateCouponCreator(Model model, @PathVariable Long eventId,
		@PathVariable Long creatorId, HttpServletResponse response) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		model.addAttribute("eventId", eventId);
		model.addAttribute("creatorId", creatorId);
		return "seller/event-update-creator";
	}

	@PutMapping("/api/seller/events/{eventId}/coupon-creators/{creatorId}")
	public String updateCouponCreator(
		UpdateCouponCreatorControllerRequest updateCouponCreatorControllerRequest,
		@PathVariable Long eventId, @PathVariable Long creatorId,
		@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			Cookie cookie = new Cookie("Authorization",
				URLEncoder.encode(token, StandardCharsets.UTF_8));
			cookie.setSecure(false);
			cookie.setMaxAge(7 * 24 * 60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		UpdateSellerCouponCreatorServiceRequest updateSellerCouponCreatorServiceRequest = new UpdateSellerCouponCreatorServiceRequest(
			updateCouponCreatorControllerRequest, eventId, creatorId, userDetails.getId());
		couponCreatorService.updateSellerCouponCreator(updateSellerCouponCreatorServiceRequest);
		model.addAttribute("message", "쿠폰생성자 수정에 성공했습니다.");
		model.addAttribute("searchUrl", "/seller/templates/event");
		return "/admin/message";
	}

	@GetMapping("/seller/templates/events/{eventId}")
	public String deleteEventSub(@PathVariable Long eventId,
		@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			Cookie cookie = new Cookie("Authorization",
				URLEncoder.encode(token, StandardCharsets.UTF_8));
			cookie.setSecure(false);
			cookie.setMaxAge(7 * 24 * 60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		deleteEvent(eventId, userDetails);
		model.addAttribute("message", "쿠폰생성자 삭제에 성공했습니다.");
		model.addAttribute("searchUrl", "/seller/templates/event");
		return "/admin/message";
	}

	@DeleteMapping("/api/seller/events/{eventId}/delete")
	public void deleteEvent(@PathVariable Long eventId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		DeleteEventServiceRequest deleteEventServiceRequest = new DeleteEventServiceRequest(eventId,
			userDetails.getId());
		eventService.deleteSellerEvent(deleteEventServiceRequest);
	}
}