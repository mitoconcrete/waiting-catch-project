package team.waitingcatch.app.user.service;

import java.util.Optional;

import team.waitingcatch.app.user.entitiy.User;

public interface InternalUserService {
	Optional<User> _findByUsername(String username);

	Optional<User> _findByEmail(String email);

}
