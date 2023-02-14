package team.waitingcatch.app.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.JwtUtil;
import team.waitingcatch.app.user.dto.CreateUserControllerRequest;
import team.waitingcatch.app.user.dto.CreateUserServiceRequest;
import team.waitingcatch.app.user.dto.DeleteUserRequest;
import team.waitingcatch.app.user.dto.FindPasswordRequest;
import team.waitingcatch.app.user.dto.GetCustomerByIdAndRoleServiceRequest;
import team.waitingcatch.app.user.dto.LoginRequest;
import team.waitingcatch.app.user.dto.LoginServiceResponse;
import team.waitingcatch.app.user.dto.UpdateUserControllerRequest;
import team.waitingcatch.app.user.dto.UpdateUserServiceRequest;
import team.waitingcatch.app.user.dto.UserInfoResponse;
import team.waitingcatch.app.user.enums.UserRoleEnum;
import team.waitingcatch.app.user.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	// global
	@PostMapping({"/customer/login", "/seller/login", "/admin/login"})
	public void login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		LoginServiceResponse loginServiceResponse = userService.login(loginRequest);
		// 엑세스 토큰을 서비스로 부터 반환 받아 헤더에 넣어준다.
		response.setHeader(JwtUtil.AUTHORIZATION_HEADER, loginServiceResponse.getAccessToken());
	}

	// customer
	@DeleteMapping("/customer/withdraw")
	public void withdrawCustomer(@AuthenticationPrincipal UserDetails userDetails) {
		DeleteUserRequest servicePayload = new DeleteUserRequest(userDetails.getUsername());
		userService.deleteUser(servicePayload);
	}

	@PostMapping("/customer/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public void createCustomer(@RequestBody @Valid CreateUserControllerRequest controllerRequest) {
		_createUserService(UserRoleEnum.USER, controllerRequest);
	}

	@PostMapping("/customer/find-password")
	public void findCustomerPassword(@RequestBody @Valid FindPasswordRequest findPasswordRequest) {
		userService.findUserAndSendEmail(findPasswordRequest);
	}

	// seller
	@DeleteMapping("/seller/withdraw")
	public void withdrawSeller(@AuthenticationPrincipal UserDetails userDetails) {
		DeleteUserRequest servicePayload = new DeleteUserRequest(userDetails.getUsername());
		userService.deleteUser(servicePayload);
	}

	@PutMapping("/seller/info")
	public void updateSellerInfo(@AuthenticationPrincipal UserDetails userDetails, @RequestBody
	UpdateUserControllerRequest controllerRequest) {
		UpdateUserServiceRequest servicePayload = new UpdateUserServiceRequest(controllerRequest.getName(),
			controllerRequest.getEmail(), userDetails.getUsername(), controllerRequest.getNickName(),
			controllerRequest.getPhoneNumber());
		userService.updateUser(servicePayload);
	}

	// admin
	@GetMapping("/admin/customers")
	public List<UserInfoResponse> getCustomers() {
		return userService.getCustomers();
	}

	@GetMapping("/admin/customers/{customerId}")
	public UserInfoResponse getCustomer(@PathVariable Long customerId) {
		GetCustomerByIdAndRoleServiceRequest servicePayload =
			new GetCustomerByIdAndRoleServiceRequest(
				customerId,
				UserRoleEnum.USER
			);

		return userService.getByUserIdAndRole(servicePayload);
	}

	@PostMapping("/admin/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public void createAdmin(@RequestBody @Valid CreateUserControllerRequest controllerRequest) {
		_createUserService(UserRoleEnum.ADMIN, controllerRequest);
	}

	private void _createUserService(UserRoleEnum role, CreateUserControllerRequest controllerRequest) {
		// 컨트롤러에서 전달받은 데이터를 기반으로 role을 할당하여 서비스에 전달한다.
		CreateUserServiceRequest servicePayload =
			new CreateUserServiceRequest(
				role,
				controllerRequest.getName(),
				controllerRequest.getEmail(),
				controllerRequest.getUsername(),
				controllerRequest.getPassword(),
				controllerRequest.getNickname(),
				controllerRequest.getPhoneNumber()
			);

		userService.createUser(servicePayload);
	}
}
