package team.waitingcatch.app.user.service;

import java.util.List;

import team.waitingcatch.app.user.dto.CustomerResponse;
import team.waitingcatch.app.user.dto.GetCustomerByIdAndRoleServiceRequest;
import team.waitingcatch.app.user.dto.UserCreateServiceRequest;

public interface UserService {
	List<CustomerResponse> getCustomers();

	CustomerResponse getByUserIdAndRole(GetCustomerByIdAndRoleServiceRequest requestPayload);

	void createUser(UserCreateServiceRequest payload);
}
