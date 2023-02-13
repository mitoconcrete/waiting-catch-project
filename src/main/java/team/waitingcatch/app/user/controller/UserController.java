package team.waitingcatch.app.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.user.dto.CustomerResponse;
import team.waitingcatch.app.user.dto.UserCreateControllerRequest;
import team.waitingcatch.app.user.dto.UserCreateServiceRequest;
import team.waitingcatch.app.user.enums.UserRoleEnum;
import team.waitingcatch.app.user.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	// customer
	// seller
	// admin
	@GetMapping("/admin/customers")
	public List<CustomerResponse> getCustomers() {
		return userService.getCustomers();
	}

	@PostMapping("/admin/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public void createAdminUser(@RequestBody UserCreateControllerRequest controllerRequest) {
		// 컨트롤러에서 전달받은 데이터를 기반으로 role을 할당하여 서비스에 전달한다.
		UserCreateServiceRequest serviceRequest =
			new UserCreateServiceRequest(
				UserRoleEnum.ADMIN,
				controllerRequest.getName(),
				controllerRequest.getEmail(),
				controllerRequest.getUsername(),
				controllerRequest.getPassword(),
				controllerRequest.getNickname(),
				controllerRequest.getPhoneNumber()
			);

		userService.createUser(serviceRequest);
	}
}
