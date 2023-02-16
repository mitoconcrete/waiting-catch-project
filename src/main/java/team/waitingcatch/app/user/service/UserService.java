package team.waitingcatch.app.user.service;

import java.util.List;

import team.waitingcatch.app.user.dto.CreateUserServiceRequest;
import team.waitingcatch.app.user.dto.DeleteUserRequest;
import team.waitingcatch.app.user.dto.FindPasswordRequest;
import team.waitingcatch.app.user.dto.GetCustomerByIdAndRoleServiceRequest;
import team.waitingcatch.app.user.dto.LoginRequest;
import team.waitingcatch.app.user.dto.LoginServiceResponse;
import team.waitingcatch.app.user.dto.LogoutRequest;
import team.waitingcatch.app.user.dto.UpdateUserServiceRequest;
import team.waitingcatch.app.user.dto.UserInfoResponse;

public interface UserService {
	LoginServiceResponse login(LoginRequest payload);

	void logout(LogoutRequest payload);

	List<UserInfoResponse> getCustomers();

	UserInfoResponse getByUserIdAndRole(GetCustomerByIdAndRoleServiceRequest payload);

	void createUser(CreateUserServiceRequest payload);

	void updateUser(UpdateUserServiceRequest payload);

	void deleteUser(DeleteUserRequest payload);

	void findUserAndSendEmail(FindPasswordRequest payload);
}
