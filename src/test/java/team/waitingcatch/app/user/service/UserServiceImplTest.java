package team.waitingcatch.app.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import team.waitingcatch.app.common.util.JwtUtil;
import team.waitingcatch.app.redis.repository.AliveTokenRepository;
import team.waitingcatch.app.user.dto.LoginRequest;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.enums.UserRoleEnum;
import team.waitingcatch.app.user.repository.UserRepository;

@SpringBootTest
@Transactional
@Rollback(value = false)
@Slf4j
class UserServiceImplTest {
	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	@Autowired
	private AliveTokenRepository aliveTokenRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtil jwtUtil;

	@BeforeEach
	void before() {
		User user = new User(UserRoleEnum.USER, "김태훈", "xogns98@gmail.com", "xogns656", "Test1234!", "mitoconcre",
			"010-1234-1234");
		userRepository.save(user);
	}

	@Test
	@DisplayName("로그인")
	void login() {
		// given
		String username = "xogns656";
		String password = "Test1234!";
		var request = mock(LoginRequest.class);
		when(request.getUsername()).thenReturn(username);
		when(request.getPassword()).thenReturn(password);

		// when
		var response = userService.login(request);
		var claims = jwtUtil.getTokenClaims(response.getAccessToken().substring(7));

		// then
		assertEquals(claims.getSubject(), username);
		assertTrue(aliveTokenRepository.existsById(response.getAccessToken().substring(7)));
	}
}