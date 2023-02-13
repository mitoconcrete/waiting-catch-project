package team.waitingcatch.app.user.service;

import java.util.List;

import team.waitingcatch.app.user.dto.CustomerResponse;
import team.waitingcatch.app.user.dto.DeleteUserRequest;
import team.waitingcatch.app.user.dto.FindPasswordRequest;
import team.waitingcatch.app.user.dto.GetCustomerByIdAndRoleServiceRequest;
import team.waitingcatch.app.user.dto.UpdateUserServiceRequest;
import team.waitingcatch.app.user.dto.UserCreateServiceRequest;

public interface UserService {
	List<CustomerResponse> getCustomers();

	CustomerResponse getByUserIdAndRole(GetCustomerByIdAndRoleServiceRequest payload);

	void createUser(UserCreateServiceRequest payload);

	void updateUser(UpdateUserServiceRequest payload);

	void deleteUser(DeleteUserRequest payload);

	void findUserAndSendEmail(FindPasswordRequest payload);
}
