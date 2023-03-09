package team.waitingcatch.app.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import team.waitingcatch.app.user.dto.CreateUserServiceRequest;
import team.waitingcatch.app.user.dto.DeleteUserRequest;
import team.waitingcatch.app.user.dto.FindPasswordRequest;
import team.waitingcatch.app.user.dto.GetCustomerByIdAndRoleServiceRequest;
import team.waitingcatch.app.user.dto.LoginRequest;
import team.waitingcatch.app.user.dto.LoginServiceResponse;
import team.waitingcatch.app.user.dto.LogoutRequest;
import team.waitingcatch.app.user.dto.UpdatePasswordServiceRequest;
import team.waitingcatch.app.user.dto.UpdateUserServiceRequest;
import team.waitingcatch.app.user.dto.UserInfoResponse;

public interface UserService {
	LoginServiceResponse login(LoginRequest payload);

	void logout(LogoutRequest payload);

	Page<UserInfoResponse> getCustomers(Pageable payload);

	UserInfoResponse getByUserIdAndRole(GetCustomerByIdAndRoleServiceRequest payload);

	void createUser(CreateUserServiceRequest payload);

	void updateUser(UpdateUserServiceRequest payload);

	void findUserAndSendEmail(FindPasswordRequest payload);

	LoginServiceResponse createAccessTokenByEmail(String email);

	void deleteCustomer(DeleteUserRequest payload);

	void deleteSeller(DeleteUserRequest payload);

	void updatePassword(UpdatePasswordServiceRequest payload);

	Page<UserInfoResponse> getCustomersByUserName(String searchVal, Pageable pageable);

}
