package team.waitingcatch.app.user.controller;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.common.util.JwtUtil;
import team.waitingcatch.app.redis.service.ValidCodeService;
import team.waitingcatch.app.user.dto.AccessTokenResonse;
import team.waitingcatch.app.user.dto.CheckValidCodeRequest;
import team.waitingcatch.app.user.dto.CreateUserControllerRequest;
import team.waitingcatch.app.user.dto.CreateUserServiceRequest;
import team.waitingcatch.app.user.dto.CreateValidCodeRequest;
import team.waitingcatch.app.user.dto.DeleteUserRequest;
import team.waitingcatch.app.user.dto.FindPasswordRequest;
import team.waitingcatch.app.user.dto.GetCustomerByIdAndRoleServiceRequest;
import team.waitingcatch.app.user.dto.LoginRequest;
import team.waitingcatch.app.user.dto.LoginServiceResponse;
import team.waitingcatch.app.user.dto.LogoutRequest;
import team.waitingcatch.app.user.dto.UpdatePasswordControllerRequest;
import team.waitingcatch.app.user.dto.UpdatePasswordServiceRequest;
import team.waitingcatch.app.user.dto.UpdateUserControllerRequest;
import team.waitingcatch.app.user.dto.UpdateUserServiceRequest;
import team.waitingcatch.app.user.dto.UserInfoResponse;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;
import team.waitingcatch.app.user.enums.UserRoleEnum;
import team.waitingcatch.app.user.service.UserService;

@RequestMapping("/api")
@RestController
@Validated
@RequiredArgsConstructor
public class UserController {

	private final JwtUtil jwtUtil;
	private final UserService userService;

	private final ValidCodeService validCodeService;

	// global
	@PostMapping({"/general/customer/signin", "/general/seller/signin", "/general/admin/signin"})
	public AccessTokenResonse login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
		LoginServiceResponse loginServiceResponse = userService.login(loginRequest);
		// 엑세스 토큰을 서비스로 부터 반환 받아 헤더에 넣어준다.
		response.setHeader(JwtUtil.AUTHORIZATION_HEADER, loginServiceResponse.getAccessToken());
		return new AccessTokenResonse(loginServiceResponse.getAccessToken());

	}

	@PostMapping({"/customer/signout", "/seller/signout", "/admin/signout"})
	public void logout(HttpServletRequest request) {
		String token = jwtUtil.resolveToken(request);
		LogoutRequest servicePayload = new LogoutRequest(token);
		userService.logout(servicePayload);
	}

	// customer
	@GetMapping("/customer/info")
	public GenericResponse<UserInfoResponse> getCustomer(@AuthenticationPrincipal UserDetails userDetails) {
		return new GenericResponse<>(new UserInfoResponse(((UserDetailsImpl)userDetails).getUser()));
	}

	@DeleteMapping("/customer/withdraw")
	public void withdrawCustomer(@AuthenticationPrincipal UserDetails userDetails) {
		DeleteUserRequest servicePayload = new DeleteUserRequest(userDetails.getUsername());
		userService.deleteCustomer(servicePayload);
	}

	@PostMapping("/general/customer/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public void createCustomer(@RequestBody @Valid CreateUserControllerRequest controllerRequest) {
		_createUserService(UserRoleEnum.USER, controllerRequest);
	}

	@PostMapping("/general/customer/find-password")
	public void findCustomerPassword(@RequestBody @Valid FindPasswordRequest findPasswordRequest) {
		userService.findUserAndSendEmail(findPasswordRequest);
	}

	@PutMapping("/customer/password")
	public void updatePassword(@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody @Valid UpdatePasswordControllerRequest controllerPayload) {
		UpdatePasswordServiceRequest servicePayload = new UpdatePasswordServiceRequest(userDetails.getUsername(),
			controllerPayload.getPassword());
		userService.updatePassword(servicePayload);
	}

	// seller
	@DeleteMapping("/seller/withdraw")
	public void withdrawSeller(@AuthenticationPrincipal UserDetails userDetails) {
		DeleteUserRequest servicePayload = new DeleteUserRequest(userDetails.getUsername());
		userService.deleteSeller(servicePayload);
		// restaturant 도 삭제
		// 관련 데이터 모두 삭제
	}

	@PutMapping("/seller/info")
	public void updateSellerInfo(@AuthenticationPrincipal UserDetails userDetails, @RequestBody
	@Valid UpdateUserControllerRequest controllerRequest) {
		UpdateUserServiceRequest servicePayload = new UpdateUserServiceRequest(controllerRequest.getName(),
			controllerRequest.getEmail(), userDetails.getUsername(), controllerRequest.getNickName(),
			controllerRequest.getPhoneNumber());
		userService.updateUser(servicePayload);
	}

	// admin
	@GetMapping("/admin/customers")
	public GenericResponse<Page<UserInfoResponse>> getCustomers(Pageable pageable) {
		return new GenericResponse<>(userService.getCustomers(pageable));
	}

	@GetMapping("/admin/customers/{customerId}")
	public GenericResponse<UserInfoResponse> getCustomer(@PathVariable Long customerId) {
		GetCustomerByIdAndRoleServiceRequest servicePayload =
			new GetCustomerByIdAndRoleServiceRequest(
				customerId,
				UserRoleEnum.USER
			);

		return new GenericResponse<>(userService.getByUserIdAndRole(servicePayload));
	}

	@GetMapping("/admin/sellers/{sellerId}")
	public GenericResponse<UserInfoResponse> getSeller(@PathVariable Long sellerId) {
		GetCustomerByIdAndRoleServiceRequest servicePayload =
			new GetCustomerByIdAndRoleServiceRequest(
				sellerId,
				UserRoleEnum.SELLER
			);

		return new GenericResponse<>(userService.getByUserIdAndRole(servicePayload));
	}

	@PostMapping("/admin/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public void createAdmin(@RequestBody @Valid CreateUserControllerRequest controllerRequest) {
		_createUserService(UserRoleEnum.ADMIN, controllerRequest);
	}

	@GetMapping("/general/google/callback")
	@ResponseStatus(HttpStatus.CREATED)
	public void createTokenByEmail(
		@RequestParam @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
			message = "이메일 형식을 맞춰주세요.") String email, HttpServletResponse response) {
		LoginServiceResponse responsePayload = userService.createAccessTokenByEmail(email);
		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, responsePayload.getAccessToken());
	}

	@PostMapping("/general/valid-code/create")
	@ResponseStatus(HttpStatus.CREATED)
	public GenericResponse<CreateValidCodeResponse> createValidCode(
		@RequestBody @Valid CreateValidCodeRequest createValidCodeRequest) throws
		UnsupportedEncodingException,
		URISyntaxException,
		NoSuchAlgorithmException,
		InvalidKeyException,
		JsonProcessingException {
		return new GenericResponse<>(validCodeService.createValidCodeByPhoneNumber(createValidCodeRequest));
	}

	@PostMapping("/general/valid-code/check")
	public void checkValidCode(@RequestBody @Valid CheckValidCodeRequest checkValidCodeRequest) {
		validCodeService.checkValidCode(checkValidCodeRequest);
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