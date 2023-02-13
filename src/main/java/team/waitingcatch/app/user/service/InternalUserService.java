package team.waitingcatch.app.user.service;

import team.waitingcatch.app.user.entitiy.User;

public interface InternalUserService {
	User _findByUsername(String username);

	User _findByEmail(String email);

}
