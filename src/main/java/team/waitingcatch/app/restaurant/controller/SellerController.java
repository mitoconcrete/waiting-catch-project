package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.common.util.JwtUtil;
import team.waitingcatch.app.restaurant.dto.menu.CreateMenuControllerRequest;
import team.waitingcatch.app.restaurant.dto.menu.CreateMenuServiceRequest;
import team.waitingcatch.app.restaurant.dto.menu.MenuResponse;
import team.waitingcatch.app.restaurant.dto.menu.UpdateMenuControllerRequest;
import team.waitingcatch.app.restaurant.dto.menu.UpdateMenuServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerControllerRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.service.menu.MenuService;
import team.waitingcatch.app.restaurant.service.requestseller.SellerManagementService;
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

	/*     로그인 프론트     */

	@GetMapping("hello")
	public String hello(Model model) {
		model.addAttribute("message", "Hello, World!");
		return "hello";
	}

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
		Address address = new Address(
			demandSignUpControllerRequest.getProvince(),
			demandSignUpControllerRequest.getCity(),
			demandSignUpControllerRequest.getStreet()
		);
		Position position = new Position(
			demandSignUpControllerRequest.getLatitude(),
			demandSignUpControllerRequest.getLongitude()
		);

		DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest = new DemandSignUpSellerServiceRequest(
			demandSignUpControllerRequest, address, position);
		sellerManagementService.demandSignUpSeller(demandSignupSellerServiceRequest);
		return "/hello";
	}

	/*     메뉴 프론트     */

	@GetMapping("/menu")
	public String menu(Model model) {
		Long restaurantId = Long.parseLong("1");
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
		//@RequestPart("images") MultipartFile multipartFile,
		@Valid CreateMenuControllerRequest request) {
		//restaurantId -> 쓸지말지 모르겠음. 임시로 값 입력 , multipartFile -> 사용법모르겠음. 임시로 값입력
		Long restaurantId = Long.parseLong("1");
		MultipartFile multipartFile = null;
		CreateMenuServiceRequest serviceRequest = new CreateMenuServiceRequest(restaurantId, multipartFile, request);
		menuService.createMenu(serviceRequest);
		return "redirect:/menu";
	}

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
		userService.deleteUser(servicePayload);
	}

	@GetMapping("/seller/infos")
	public String updateSellerInfoSub(@AuthenticationPrincipal UserDetails userDetails) {
		System.out.println(userDetails + "111111111111");
		return "seller-infos";
	}

	@PostMapping("/seller/infos")
	public String updateSellerInfo(@AuthenticationPrincipal UserDetails userDetails,
		@Valid UpdateUserControllerRequest controllerRequest) {
		System.out.println(userDetails + "22222222222222");
		UpdateUserServiceRequest servicePayload = new UpdateUserServiceRequest(controllerRequest.getName(),
			controllerRequest.getEmail(), userDetails.getUsername(), controllerRequest.getNickName(),
			controllerRequest.getPhoneNumber());
		userService.updateUser(servicePayload);
		return "redirect:/seller";
	}

	// //판매자가 자신의 레스토랑 정보를 수정한다.
	// @PutMapping("/restaurant/info")
	// public void updateRestaurant(
	// 	@RequestPart("updateRestaurantRequest") UpdateRestaurantControllerRequest updateRestaurantControllerRequest,
	// 	@RequestPart("images") List<MultipartFile> multipartFile,
	// 	@AuthenticationPrincipal UserDetails userDetails) throws IOException {
	// 	UpdateRestaurantServiceRequest updateRestaurantServiceRequest =
	// 		new UpdateRestaurantServiceRequest(updateRestaurantControllerRequest, multipartFile,
	// 			userDetails.getUsername());
	//
	// 	restaurantService.updateRestaurant(updateRestaurantServiceRequest);
	// }

	@PostMapping("/getToken")
	public String Token(@RequestParam("jwtToken") String jwtToken) {
		String token = jwtToken;
		// jwtToken을 원하는 변수에 저장합니다.
		return token;
	}

}