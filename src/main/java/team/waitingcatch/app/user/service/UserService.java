package team.waitingcatch.app.user.service;

import team.waitingcatch.app.user.dto.UserCreateServiceRequest;
import team.waitingcatch.app.user.entitiy.User;

public interface UserService {

	User createUser(UserCreateServiceRequest payload);
}
