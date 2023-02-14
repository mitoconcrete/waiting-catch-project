package team.waitingcatch.app.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.user.dto.UserCreateServiceRequest;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, InternalUserService {

	private final UserRepository userRepository;

	@Override
	public Optional<User> _findByUsername(String username) {
		return Optional.empty();
	}

	@Override
	public Optional<User> _findByEmail(String email) {
		return Optional.empty();
	}

	public User createUser(UserCreateServiceRequest payload) {
		Boolean isExistUser = userRepository.existsByUsername(payload.getUsername());
		if (isExistUser) {
			throw new IllegalArgumentException("이미 존재하는 유저입니다.");
		}
		User newUser =
			new User(
				payload.getRole(),
				payload.getUsername(),
				payload.getPassword(),
				payload.getPhoneNumber(),
				payload.getEmail(),
				payload.getName()
			);
		userRepository.save(newUser);
		// 새로운 유저를 생성하고 저장합니다.

		return newUser;
	}
}

