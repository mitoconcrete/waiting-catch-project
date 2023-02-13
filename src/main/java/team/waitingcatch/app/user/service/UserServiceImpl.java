package team.waitingcatch.app.user.service;

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
	public User _findByUsername(String username) {
		return null;
	}

	@Override
	public User _findByEmail(String email) {
		return null;
	}

	public User createSeller(UserCreateServiceRequest payload) {
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

