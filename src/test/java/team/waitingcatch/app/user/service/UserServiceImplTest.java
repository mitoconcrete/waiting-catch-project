package team.waitingcatch.app.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import team.waitingcatch.app.common.util.JwtUtil;
import team.waitingcatch.app.redis.repository.AliveTokenRepository;
import team.waitingcatch.app.redis.repository.KilledAccessTokenRepository;
import team.waitingcatch.app.user.dto.LoginRequest;
import team.waitingcatch.app.user.dto.LogoutRequest;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.enums.UserRoleEnum;
import team.waitingcatch.app.user.repository.UserRepository;

@SpringBootTest
@Transactional
@Rollback(value = true)
@Slf4j
class UserServiceImplTest {
	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	@Autowired
	private KilledAccessTokenRepository killedAccessTokenRepository;

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

	@Test
	@DisplayName("로그아웃")
	void logout() {
		// given
		String username = "xogns656";
		String password = "Test1234!";
		var loginRequest = mock(LoginRequest.class);
		when(loginRequest.getUsername()).thenReturn(username);
		when(loginRequest.getPassword()).thenReturn(password);
		var loginResponse = userService.login(loginRequest);

		var logoutRequest = mock(LogoutRequest.class);
		when(logoutRequest.getAccessToken()).thenReturn(loginResponse.getAccessToken().substring(7));

		// when
		userService.logout(logoutRequest);

		// then
		assertFalse(aliveTokenRepository.existsById(loginResponse.getAccessToken().substring(7)));
		assertTrue(killedAccessTokenRepository.existsById(loginResponse.getAccessToken().substring(7)));
	}

	@Test
	@DisplayName("유저 목록 등록 후 페이징 처리")
	void getCustomers() {
		// given
		String[] usernameList = {
			"xogns1", "xogns2", "xogns3", "xogns4"
		};

		String[] nicknameList = {
			"xogns1", "xogns2", "xogns3", "xogns4"
		};

		String[] pnList = {
			"010-1001-1234", "010-1002-1234", "010-1003-1234", "010-1004-1234"
		};

		String[] emailList = {
			"1@email.com", "2@email.com", "3@email.com", "4@email.com"
		};

		for (int i = 0; i < usernameList.length; i++) {
			var user = new User(UserRoleEnum.USER, "김태훈", emailList[i], usernameList[i], "Test1234!",
				nicknameList[i], pnList[i]);
			userRepository.save(user);
		}

		// create seller , admin
		User seller = new User(UserRoleEnum.SELLER, "김태훈", "1@seller.com", "seller01", "Test1234!", "seller01",
			"010-1234-0001");
		User admin = new User(UserRoleEnum.ADMIN, "김태훈", "1@admin.com", "admin01", "Test1234!", "seller01",
			"010-1234-0002");
		// when&then
		Pageable pageable1 = Pageable.ofSize(3).withPage(0);
		Pageable pageable2 = Pageable.ofSize(3).withPage(1);
		Pageable pageable3 = Pageable.ofSize(3).withPage(2);

		Pageable pageable4 = Pageable.ofSize(5).withPage(0);
		Pageable pageable5 = Pageable.ofSize(5).withPage(1);
		Pageable pageable6 = Pageable.ofSize(5).withPage(2);
		assertEquals(userService.getCustomers(pageable1).size(), 3);
		assertEquals(userService.getCustomers(pageable2).size(), 2);
		assertEquals(userService.getCustomers(pageable3).size(), 0);

		assertEquals(userService.getCustomers(pageable4).size(), 5);
		assertEquals(userService.getCustomers(pageable5).size(), 0);
		assertEquals(userService.getCustomers(pageable6).size(), 0);
	}
}