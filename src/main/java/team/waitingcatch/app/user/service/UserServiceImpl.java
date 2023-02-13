package team.waitingcatch.app.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.user.dto.CustomerResponse;
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
}

