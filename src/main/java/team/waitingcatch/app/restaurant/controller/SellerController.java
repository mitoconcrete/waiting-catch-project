package team.waitingcatch.app.restaurant.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
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
import team.waitingcatch.app.restaurant.dto.menu.MenuResponse;
import team.waitingcatch.app.restaurant.dto.menu.UpdateMenuControllerRequest;
import team.waitingcatch.app.restaurant.dto.menu.UpdateMenuServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerControllerRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantControllerRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.service.menu.MenuService;
import team.waitingcatch.app.restaurant.service.requestseller.SellerManagementService;
import team.waitingcatch.app.restaurant.service.restaurant.RestaurantService;
import team.waitingcatch.app.user.dto.DeleteUserRequest;
import team.waitingcatch.app.user.dto.LoginRequest;
import team.waitingcatch.app.user.dto.LoginServiceResponse;
import team.waitingcatch.app.user.dto.LogoutRequest;
import team.waitingcatch.app.user.dto.UpdateUserControllerRequest;
import team.waitingcatch.app.user.dto.UpdateUserServiceRequest;
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
	/*     로그인 프론트     */

	// @GetMapping("hello")
	// public String hello(Model model) {
	// 	model.addAttribute("message", "Hello, World!");
	// 	return "hello";
	// }

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@ResponseBody
	@PostMapping("/login")
	public String login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		LoginServiceResponse loginServiceResponse = userService.login(loginRequest);
		// 엑세스 토큰을 서비스로 부터 반환 받아 헤더에 넣어준다.
		response.setHeader(JwtUtil.AUTHORIZATION_HEADER, loginServiceResponse.getAccessToken());
		return "success";
	}

	@GetMapping("/signup")
	public String signup() {
		return "register";
	}

	@PostMapping("/signup")
	public String demandSignUpSeller(
		@Valid DemandSignUpSellerControllerRequest demandSignUpControllerRequest) {
		System.out.println(demandSignUpControllerRequest.getPhoneNumber());
		// Address address = new Address(
		// 	demandSignUpControllerRequest.getProvince(),
		// 	demandSignUpControllerRequest.getCity(),
		// 	demandSignUpControllerRequest.getStreet()
		// );
		// Position position = new Position(
		// 	demandSignUpControllerRequest.getLatitude(),
		// 	demandSignUpControllerRequest.getLongitude()
		// );

		DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest = new DemandSignUpSellerServiceRequest(
			demandSignUpControllerRequest);
		sellerManagementService.demandSignUpSeller(demandSignupSellerServiceRequest);
		return "/hello";
	}

	/*     메뉴 프론트     */

	@GetMapping("/menu")
	public String menu(Model model) {
		Long restaurantId = Long.parseLong("4");
		List<MenuResponse> menus = menuService.getMyRestaurantMenus(restaurantId);
		model.addAttribute("menus", menus);
		return "menu";
	}

	@GetMapping("/menu/new")
	public String createMenu() {
		return "menu-new";
	}

	//@PostMapping("/seller/restaurants/{restaurantId}/menus")
	@PostMapping("/menu/new")
	@ResponseStatus(value = HttpStatus.CREATED)
	public String createMenu(
		@RequestPart("image") MultipartFile multipartFile,
		@Valid CreateMenuControllerRequest request) {
		//MultipartFile multipartFile = null;
		Long restaurantId = Long.parseLong("4");
		System.out.println(multipartFile + " dd " + request + " dd ");
		CreateMenuServiceRequest serviceRequest = new CreateMenuServiceRequest(restaurantId, multipartFile, request);
		menuService.createMenu(serviceRequest);
		return "redirect:/menu";
	}

	// // seller
	// @PostMapping("/seller/restaurants/{restaurantId}/menus")
	// @ResponseStatus(value = HttpStatus.CREATED)
	// public void createMenu(@PathVariable Long restaurantId,
	// 	@RequestPart("images") MultipartFile multipartFile,
	// 	@RequestPart("requestDto") @Valid CreateMenuControllerRequest request) {
	//
	// 	CreateMenuServiceRequest serviceRequest = new CreateMenuServiceRequest(restaurantId, multipartFile, request);
	// 	menuService.createMenu(serviceRequest);
	// }

	@GetMapping("/menu/update/{menuId}")
	public String updateMenu(Model model, @PathVariable Long menuId) {
		model.addAttribute("menuId", menuId);
		return "menu-update";
	}

	@PutMapping("/menu/update/{menuId}")
	public String updateMenu(@PathVariable Long menuId,
		//@RequestPart("images") MultipartFile multipartFile,
		@Valid UpdateMenuControllerRequest request) {
		MultipartFile multipartFile = null;
		UpdateMenuServiceRequest serviceRequest = new UpdateMenuServiceRequest(menuId, request, multipartFile);
		menuService.updateMenu(serviceRequest);
		return "redirect:/menu";
	}

	@GetMapping("/menu/delete/{menuId}")
	public String deleteMenuSub(@PathVariable Long menuId) {
		deleteMenu(menuId);
		return "redirect:/menu";
	}

	@DeleteMapping("/menu/delete/{menuId}")
	public void deleteMenu(@PathVariable Long menuId) {
		menuService.deleteMenu(menuId);
	}

	/*     판매자 정보 프론트   */
	@GetMapping("/seller")
	public String seller() {
		return "seller";
	}

	@GetMapping("/seller/logout")
	public String logoutSub(HttpServletRequest request) {
		String token = jwtUtil.resolveToken(request);
		System.out.println("토큰배달" + token);
		LogoutRequest servicePayload = new LogoutRequest(token);
		userService.logout(servicePayload);
		return "redirect:/hello";
	}

	@GetMapping("/seller/delete")
	public String withdrawSellerSub(@AuthenticationPrincipal UserDetails userDetails) {
		System.out.println(userDetails + "디테일");
		withdrawSeller(userDetails);
		return "redirect:/hello";
	}

	@DeleteMapping("/seller/delete")
	public void withdrawSeller(@AuthenticationPrincipal UserDetails userDetails) {
		DeleteUserRequest servicePayload = new DeleteUserRequest(userDetails.getUsername());
		// userService.deleteUser(servicePayload);
	}

	@GetMapping("/seller/infos")
	public String updateSellerInfoSub() {
		return "seller-infos";
	}

	@PutMapping("/seller/infos")
	public String updateSellerInfo(@Valid UpdateUserControllerRequest controllerRequest) {
		String username = "song1";
		UpdateUserServiceRequest servicePayload = new UpdateUserServiceRequest(controllerRequest.getName(),
			controllerRequest.getEmail(), username, controllerRequest.getNickName(),
			controllerRequest.getPhoneNumber());
		userService.updateUser(servicePayload);
		return "redirect:/seller";
	}

	@GetMapping("/seller/updaterestaurant")
	public String updateRestaurantSub() {
		return "seller-restaurantupdate";
	}

	@PutMapping("/seller/updaterestaurant")
	public String updateRestaurant(
		UpdateRestaurantControllerRequest updateRestaurantControllerRequest) throws IOException {
		Long userId = Long.parseLong("1");
		List<MultipartFile> multipartFile = null;
		UpdateRestaurantServiceRequest updateRestaurantServiceRequest =
			new UpdateRestaurantServiceRequest(updateRestaurantControllerRequest, multipartFile,
				userId);

		restaurantService.updateRestaurant(updateRestaurantServiceRequest);

		return "redirect:/seller";
	}





	/*     이벤트     */

	@GetMapping("/event")
	public String event(Model model) {
		Long restaurantId = Long.parseLong("4");
		List<GetEventsResponse> events = eventService.getRestaurantEvents(restaurantId);
		model.addAttribute("events", events);
		return "event";
	}

	@GetMapping("/event/new")
	public String createEvent() {
		return "event-create";
	}

	@PostMapping("/event/new")
	@ResponseStatus(value = HttpStatus.CREATED)
	public String createEvent(
		@Validated CreateEventControllerRequest createEventControllerRequest) {
		Long restaurantId = Long.parseLong("4");
		CreateEventServiceRequest createEventServiceRequest = new CreateEventServiceRequest(
			createEventControllerRequest, restaurantId);
		eventService.createSellerEvent(createEventServiceRequest);
		return "redirect:/event";
	}

	@GetMapping("/event/couponcreator/{eventId}")
	public String createCouponCreator(Model model, @PathVariable Long eventId) {
		model.addAttribute("eventId", eventId);
		return "event-createcreator";
	}

	@PostMapping("/event/couponcreator/{eventId}")
	@ResponseStatus(value = HttpStatus.CREATED)
	public String createCouponCreator(@PathVariable Long eventId,
		@Validated CreateCouponCreatorControllerRequest createCouponCreatorControllerRequest) {
		Long userId = Long.parseLong("1");

		CreateSellerCouponCreatorServiceRequest createSellerCouponCreatorServiceRequest = new CreateSellerCouponCreatorServiceRequest(
			createCouponCreatorControllerRequest, eventId, userId);

		couponCreatorService.createSellerCouponCreator(createSellerCouponCreatorServiceRequest);
		return "redirect:/event";
	}

	@GetMapping("/event/update/{eventId}")
	public String updateEvent(Model model, @PathVariable Long eventId) {
		model.addAttribute("eventId", eventId);
		return "event-update";
	}

	@PutMapping("/event/update/{eventId}")
	public String updateEvent(UpdateEventControllerRequest updateEventControllerRequest,
		@PathVariable Long eventId) {
		System.out.println("아이디 " + eventId + "값" + updateEventControllerRequest.getName());
		Long userId = Long.parseLong("1");
		UpdateSellerEventServiceRequest updateSellerEventServiceRequest = new UpdateSellerEventServiceRequest(
			updateEventControllerRequest, eventId, userId);
		eventService.updateSellerEvent(updateSellerEventServiceRequest);
		return "redirect:/event";
	}

	@GetMapping("/event/updateCouponCreator/{eventId}/{creatorId}")
	public String updateCouponCreator(Model model, @PathVariable Long eventId, @PathVariable Long creatorId) {
		model.addAttribute("eventId", eventId);
		model.addAttribute("creatorId", creatorId);
		return "event-updatecreator";
	}

	@PutMapping("/event/updateCouponCreator/{eventId}/{creatorId}")
	public String updateCouponCreator(
		UpdateCouponCreatorControllerRequest updateCouponCreatorControllerRequest,
		@PathVariable Long eventId, @PathVariable Long creatorId) {
		Long userId = Long.parseLong("1");
		UpdateSellerCouponCreatorServiceRequest updateSellerCouponCreatorServiceRequest = new UpdateSellerCouponCreatorServiceRequest(
			updateCouponCreatorControllerRequest, eventId, creatorId, userId);
		couponCreatorService.updateSellerCouponCreator(updateSellerCouponCreatorServiceRequest);
		return "redirect:/event";
	}

	@GetMapping("/event/delete/{eventId}")
	public String deleteEventSub(@PathVariable Long eventId) {
		deleteEvent(eventId);
		return "redirect:/event";
	}

	@DeleteMapping("/event/delete/{eventId}")
	public void deleteEvent(@PathVariable Long eventId) {
		Long userId = Long.parseLong("1");
		DeleteEventServiceRequest deleteEventServiceRequest = new DeleteEventServiceRequest(eventId,
			userId);
		eventService.deleteSellerEvent(deleteEventServiceRequest);
	}

}