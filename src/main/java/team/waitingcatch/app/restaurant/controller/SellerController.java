package team.waitingcatch.app.restaurant.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.common.util.JwtUtil;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerControllerRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.service.requestseller.SellerManagementService;
import team.waitingcatch.app.user.dto.LoginRequest;
import team.waitingcatch.app.user.dto.LoginServiceResponse;
import team.waitingcatch.app.user.service.UserService;

@Controller
@RequiredArgsConstructor
public class SellerController {

	private final SellerManagementService sellerManagementService;
	private final JwtUtil jwtUtil;
	private final UserService userService;


	/*     로그인 프론트 연결    */

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



	/*     메뉴 프론트 연결    */

}
