package team.waitingcatch.app.user.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.user.dto.CustomerResponse;
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
}
