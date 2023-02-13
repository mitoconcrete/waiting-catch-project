package team.waitingcatch.app.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.user.entitiy.User;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, InternalUserService {
	@Override
	public User _findByUsername(String username) {
		return null;
	}

	@Override
	public User _findByEmail(String email) {
		return null;
	}

}

