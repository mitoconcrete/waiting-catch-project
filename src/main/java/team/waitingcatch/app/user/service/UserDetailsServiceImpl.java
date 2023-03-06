package team.waitingcatch.app.user.service;

import static team.waitingcatch.app.exception.ErrorCode.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;
import team.waitingcatch.app.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsernameAndIsDeletedFalse(username)
			.orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND_USER.getMessage()));
		return new UserDetailsImpl(user);
	}
}