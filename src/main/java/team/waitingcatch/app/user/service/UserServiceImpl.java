package team.waitingcatch.app.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.user.dto.CustomerResponse;
import team.waitingcatch.app.user.dto.UserCreateServiceRequest;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, InternalUserService {
	private final UserRepository userRepository;

	@Override
	public List<CustomerResponse> getCustomers() {
		return userRepository.findAll().stream().map(CustomerResponse::new).collect(Collectors.toList());
	}

	@Override
	public void createUser(UserCreateServiceRequest payload) {
		// 이미 존재하는 유저인지 검증합니다.
		Boolean isExistUser = userRepository.existsByUsername(payload.getUsername());

		if (isExistUser) {
			throw new IllegalArgumentException("이미 존재하는 유저입니다.");
		}

		// 새로운 유저를 생성하고 저장합니다.
		User newUser =
			new User(
				payload.getRole(),
				payload.getName(),
				payload.getEmail(),
				payload.getUsername(),
				payload.getPassword(),
				payload.getNickname()
			);

		userRepository.save(newUser);
	}

}

