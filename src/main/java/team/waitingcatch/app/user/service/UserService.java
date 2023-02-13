package team.waitingcatch.app.user.service;

import java.util.List;

import team.waitingcatch.app.user.dto.CustomerResponse;

public interface UserService {
	List<CustomerResponse> getCustomers();
}
