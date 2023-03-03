package team.waitingcatch.app.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.common.util.JwtUtil;
import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.CreateEventRequest;
import team.waitingcatch.app.event.dto.event.CreateEventServiceRequest;
import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.event.repository.EventRepository;
import team.waitingcatch.app.lineup.dto.CreateReviewEntityRequest;
import team.waitingcatch.app.lineup.dto.StartWaitingServiceRequest;
import team.waitingcatch.app.lineup.entity.LineupHistory;
import team.waitingcatch.app.lineup.entity.Review;
import team.waitingcatch.app.lineup.entity.WaitingNumber;
import team.waitingcatch.app.lineup.repository.LineupHistoryRepository;
import team.waitingcatch.app.lineup.repository.LineupRepository;
import team.waitingcatch.app.lineup.repository.ReviewRepository;
import team.waitingcatch.app.lineup.repository.WaitingNumberRepository;
import team.waitingcatch.app.lineup.service.LineupService;
import team.waitingcatch.app.redis.repository.AliveTokenRepository;
import team.waitingcatch.app.redis.repository.KilledAccessTokenRepository;
import team.waitingcatch.app.restaurant.dto.restaurant.SaveDummyRestaurantRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.RestaurantInfo;
import team.waitingcatch.app.restaurant.repository.RestaurantInfoRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.user.dto.CreateUserServiceRequest;
import team.waitingcatch.app.user.dto.DeleteUserRequest;
import team.waitingcatch.app.user.dto.GetCustomerByIdAndRoleServiceRequest;
import team.waitingcatch.app.user.dto.LoginRequest;
import team.waitingcatch.app.user.dto.LogoutRequest;
import team.waitingcatch.app.user.dto.UpdatePasswordServiceRequest;
import team.waitingcatch.app.user.dto.UpdateUserServiceRequest;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.enums.UserRoleEnum;
import team.waitingcatch.app.user.repository.UserRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
@Slf4j
class UserServiceImplTest {
	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	@Autowired
	private RestaurantInfoRepository restaurantInfoRepository;
	@Autowired
	private LineupHistoryRepository lineupHistoryRepository;
	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private LineupRepository lineupRepository;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private LineupService lineupService;
	@Autowired
	private KilledAccessTokenRepository killedAccessTokenRepository;
	@Autowired
	private WaitingNumberRepository waitingNumberRepository;
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

		User seller = new User(UserRoleEnum.SELLER, "김태훈", "1@seller.com", "seller01", "Test1234!", "seller01",
			"010-1234-0001");
		userRepository.save(seller);
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
		User admin = new User(UserRoleEnum.ADMIN, "김태훈", "1@admin.com", "admin01", "Test1234!", "admin01",
			"010-1234-0002");
		userRepository.save(admin);
		// when&then
		Pageable pageable1 = Pageable.ofSize(3).withPage(0);
		Pageable pageable2 = Pageable.ofSize(3).withPage(1);
		Pageable pageable3 = Pageable.ofSize(3).withPage(2);

		Pageable pageable4 = Pageable.ofSize(5).withPage(0);
		Pageable pageable5 = Pageable.ofSize(5).withPage(1);
		Pageable pageable6 = Pageable.ofSize(5).withPage(2);
		assertThat(userService.getCustomers(pageable1).getContent().size()).isEqualTo(3);
		assertThat(userService.getCustomers(pageable2).getContent().size()).isEqualTo(3);
		assertThat(userService.getCustomers(pageable3).getContent().size()).isEqualTo(1);

		assertThat(userService.getCustomers(pageable4).getContent().size()).isEqualTo(5);
		assertThat(userService.getCustomers(pageable5).getContent().size()).isEqualTo(2);
		assertThat(userService.getCustomers(pageable6).getContent().size()).isEqualTo(0);
	}

	@Test
	@DisplayName("유저 롤과 아이디로 정보가져오기")
	void getByUserIdAndRole() {
		// given
		var seller = userRepository.findByUsernameAndIsDeletedFalse("seller01").get();

		var request = mock(GetCustomerByIdAndRoleServiceRequest.class);
		when(request.getRole()).thenReturn(UserRoleEnum.SELLER);
		when(request.getUserId()).thenReturn(seller.getId());

		// when
		var response = userService.getByUserIdAndRole(request);

		when(request.getRole()).thenReturn(UserRoleEnum.USER);

		assertEquals(response.getId(), seller.getId());
		assertEquals(response.getUsername(), seller.getUsername());
		assertThrows(IllegalArgumentException.class, () -> userService.getByUserIdAndRole(request));

		when(request.getUserId()).thenReturn(seller.getId() * 10000);
		assertThrows(IllegalArgumentException.class, () -> userService.getByUserIdAndRole(request));
	}

	@Test
	@DisplayName("유저 생성")
	void creatUser() {
		// given
		var payload1 = new CreateUserServiceRequest(
			UserRoleEnum.ADMIN,
			"김태훈",
			"1@email.com",
			"xogns1",
			"Test1234!",
			"admin01",
			"010-1001-1234"
		);

		var payload2 = new CreateUserServiceRequest(
			UserRoleEnum.ADMIN,
			"김태훈",
			"2@email.com",
			"xogns1",
			"Test1234!",
			"admin02",
			"010-1002-1234"
		);

		// when & then
		userService.createUser(payload1);
		assertTrue(userRepository.existsByUsername(payload1.getUsername()));
		assertThrows(IllegalArgumentException.class, () -> userService.createUser(payload2));
	}

	@Test
	@DisplayName("유저 수정")
	void updateUser() {
		// given
		String username = "xogns656";
		String password = "Test1234!";
		var payload = new UpdateUserServiceRequest("김센세", "2@email.com", username, "changenick", "010-1111-1111");
		var payload2 = new UpdateUserServiceRequest("김센세", "2@email.com", "1123123", "changenick", "010-1111-1111");
		// when
		userService.updateUser(payload);

		// then
		assertEquals(userRepository.findByUsernameAndIsDeletedFalse(username).get().getName(), payload.getName());
		assertEquals(userRepository.findByUsernameAndIsDeletedFalse(username).get().getEmail(), payload.getEmail());
		assertEquals(userRepository.findByUsernameAndIsDeletedFalse(username).get().getNickname(),
			payload.getNickName());
		assertEquals(userRepository.findByUsernameAndIsDeletedFalse(username).get().getPhoneNumber(),
			payload.getPhoneNumber());
		assertThrows(IllegalArgumentException.class, () -> userService.updateUser(payload2));
	}

	@Test
	@DisplayName("Oauth 로그인 서비스")
	void createAccessTokenByEmail() {
		// given
		var allowedUsername = "xogns656";
		var allowedEmail = "xogns98@gmail.com";
		var disabledEmail = "1@email.com";

		// when
		var response = userService.createAccessTokenByEmail(allowedEmail);

		// then
		var claims = jwtUtil.getTokenClaims(response.getAccessToken().substring(7));

		// then
		assertEquals(claims.getSubject(), allowedUsername);
		assertTrue(aliveTokenRepository.existsById(response.getAccessToken().substring(7)));
		assertThrows(IllegalArgumentException.class, () -> userService.createAccessTokenByEmail(disabledEmail));
	}

	@Test
	@DisplayName("유저 삭제")
	void deleteCustomer() {
		// given
		var allowedUsername = "xogns656";
		var disablesUsername = "xogns98";
		var request = mock(DeleteUserRequest.class);
		when(request.getUsername()).thenReturn(allowedUsername);

		// when
		userService.deleteCustomer(request);

		// then
		assertFalse(userRepository.findByUsernameAndIsDeletedFalse(allowedUsername).isPresent());

		when(request.getUsername()).thenReturn(disablesUsername);
		assertThrows(IllegalArgumentException.class, () -> userService.deleteCustomer(request));
	}

	@Test
	@DisplayName("셀러 삭제")
	void deleteSeller() {
		// given
		var customer = userRepository.findByUsernameAndIsDeletedFalse("xogns656").get();
		var seller = userRepository.findByUsernameAndIsDeletedFalse("seller01").get();

		// 레스토랑
		var payload = new SaveDummyRestaurantRequest("이이", "12345", "1 2 3", "1", new Position(0, 0), "1234", "1",
			seller);
		var restaurant = new Restaurant(payload);
		var createdRestaurant = restaurantRepository.save(restaurant);
		var info = new RestaurantInfo(createdRestaurant, "", "");
		info.openLineup();
		restaurantInfoRepository.save(info);

		var waiting = WaitingNumber.createWaitingNumber(createdRestaurant);
		waitingNumberRepository.save(waiting);

		// 줄서기
		lineupService.startWaiting(
			new StartWaitingServiceRequest(customer, createdRestaurant.getId(), 0, 0, 0, LocalDateTime.now()));
		var lineup = lineupRepository.findAllByUserId(customer.getId())
			.get(0);

		// 줄서기 히스토리
		var history = new LineupHistory(lineup);
		history = lineupHistoryRepository.save(history);

		// 리뷰
		var review = Review.craeteReview(
			new CreateReviewEntityRequest(customer, restaurant, 0, " ", new ArrayList<>()));
		review = reviewRepository.save(review);

		// 이벤트
		var eventpayload = mock(CreateEventControllerRequest.class);
		when(eventpayload.getEventStartDate()).thenReturn(LocalDateTime.now());
		when(eventpayload.getEventEndDate()).thenReturn(LocalDateTime.now());
		when(eventpayload.getName()).thenReturn("이벤트");

		var eventpayload1 = new CreateEventServiceRequest(eventpayload, restaurant.getId());
		var eventpayload2 = new CreateEventRequest(eventpayload1, restaurant);
		var event = new Event(eventpayload2);
		event = eventRepository.save(event);

		var servicePayload = mock(DeleteUserRequest.class);
		when(servicePayload.getUsername()).thenReturn(seller.getUsername());

		// when
		userService.deleteSeller(servicePayload);

		// then
		assertTrue(userRepository.findById(seller.getId()).get().isDeleted());
		assertTrue(restaurantRepository.findByUserId(seller.getId()).get().isDeleted());
		assertTrue(lineupHistoryRepository.findById(history.getId()).get().isDeleted());
		assertTrue(lineupRepository.findById(lineup.getId()).get().isDeleted());
		assertTrue(reviewRepository.findById(review.getId()).get().isDeleted());
		assertTrue(eventRepository.findById(event.getId()).get().isDeleted());
	}

	@Test
	@DisplayName("패스워드 업데이트 후 재 로그인")
	void updatePassword() {
		// given
		var username = "xogns656";
		var beforePW = "Test1234!";
		var afterPW = "Test123!!";
		var servicePayload = new UpdatePasswordServiceRequest("xogns656", afterPW);
		var wrongPayload = new UpdatePasswordServiceRequest("xogns98", afterPW);

		// when
		userService.updatePassword(servicePayload);

		// then
		var request = mock(LoginRequest.class);
		when(request.getUsername()).thenReturn(username);
		when(request.getPassword()).thenReturn(afterPW);

		var response = userService.login(request);
		var claims = jwtUtil.getTokenClaims(response.getAccessToken().substring(7));

		assertEquals(claims.getSubject(), username);
		assertTrue(aliveTokenRepository.existsById(response.getAccessToken().substring(7)));
		when(request.getPassword()).thenReturn(beforePW);
		assertThrows(IllegalArgumentException.class, () -> userService.login(request));

		assertThrows(IllegalArgumentException.class, () -> userService.updatePassword(wrongPayload));
	}
}