package team.waitingcatch.app.lineup;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.lineup.dto.StartWaitingServiceRequest;
import team.waitingcatch.app.lineup.repository.LineupRepository;
import team.waitingcatch.app.lineup.service.LineupService;
import team.waitingcatch.app.restaurant.dto.restaurant.SaveDummyRestaurantRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.enums.UserRoleEnum;
import team.waitingcatch.app.user.repository.UserRepository;

@SpringBootTest
@Transactional
@Rollback(value = false)
@Slf4j
class LineupIntegrationTest {
	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	@Autowired
	LineupService lineupService;

	@Autowired
	LineupRepository lineupRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RestaurantRepository restaurantRepository;

	// @BeforeEach
	// @Transactional(propagation = Propagation.REQUIRES_NEW)
	// @Rollback(value = false)
	// public void beforeEach() {
	// 	User customer = new User(UserRoleEnum.USER, "유저1", "aaa@gmail.com", "customerId", "pw12", "sj",
	// 		"01012341234");
	// 	userRepository.save(customer);
	// 	User seller = new User(UserRoleEnum.SELLER, "판매자1", "bbb@gmail.com", "sellerId", "pw123", "sjsj",
	// 		"01012312312");
	// 	userRepository.save(seller);
	//
	// 	Restaurant restaurant = new Restaurant(
	// 		new SaveDummyRestaurantRequest("레스토랑1", new Address("서울시", "강남구", "강남대로"), new Position(0.0, 0.0),
	// 			"01000000000", "일식>스시>오마카세", seller));
	// 	restaurantRepository.save(restaurant);
	// }

	@Test
	@Commit
	@Order(1)
	void test1() {
		// User customer = new User(UserRoleEnum.USER, "유저1", "aaa@gmail.com", "customerId", "pw12", "sj",
		// 	"01012341234");
		// userRepository.save(customer);
		User seller = new User(UserRoleEnum.SELLER, "판매자1", "bbb@gmail.com", "sellerId", "pw123", "sjsj",
			"01012312312");
		userRepository.save(seller);

		Restaurant restaurant = new Restaurant(
			new SaveDummyRestaurantRequest("레스토랑1", new Address("서울시", "강남구", "강남대로"), new Position(0.0, 0.0),
				"01000000000", "일식>스시>오마카세", seller));
		restaurantRepository.save(restaurant);

		List<Long> customerList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			User customer = new User(UserRoleEnum.USER, "이름" + i, i + "@gmail.com", "customerId" + i, "pw" + i,
				"sj" + i, "0101212121" + i);
			userRepository.save(customer);
			customerList.add(customer.getId());
		}
	}

	// @Test
	// @DisplayName("줄서기 요청")
	// void startWaiting() {
	// 	User customer = userRepository.findByUsernameAndIsDeletedFalse("customerId").get();
	// 	Long sellerId = userRepository.findByUsernameAndIsDeletedFalse("sellerId").get().getId();
	// 	Restaurant restaurant = restaurantRepository.findByUserId(sellerId).get();
	// 	lineupService.startWaiting(
	// 		new StartWaitingServiceRequest(customer, restaurant.getId(), 5, LocalDateTime.now()));
	//
	// 	Lineup lineup = lineupRepository.findAll().get(0);
	//
	// 	assertThat(lineup.getWaitingNumber()).isEqualTo(1);
	// 	assertThat(lineup.getNumOfMembers()).isEqualTo(5);
	// 	assertThat(lineup.getCallCount()).isEqualTo(0);
	// 	assertThat(lineup.getStatus()).isEqualTo(ArrivalStatusEnum.WAIT);
	// }
	//
	// @Test
	// @DisplayName("줄서기 취소")
	// void cancelWaiting() {
	// 	User customer1 = userRepository.findByUsernameAndIsDeletedFalse("customerId").get();
	// 	Long sellerId1 = userRepository.findByUsernameAndIsDeletedFalse("sellerId").get().getId();
	// 	Restaurant restaurant1 = restaurantRepository.findByUserId(sellerId1).get();
	// 	lineupService.startWaiting(
	// 		new StartWaitingServiceRequest(customer1, restaurant1.getId(), 5, LocalDateTime.now()));
	//
	// 	User seller2 = new User(UserRoleEnum.SELLER, "판매자2", "bbb112523@gmail.com", "sellerId999", "pw123", "www",
	// 		"01022233322");
	// 	userRepository.save(seller2);
	// 	Restaurant restaurant2 = new Restaurant(
	// 		new SaveDummyRestaurantRequest("레스토랑1", new Address("서울시", "강남구", "강남대로"), new Position(0.0, 0.0),
	// 			"01000000000", "일식>스시>오마카세", seller2));
	// 	restaurantRepository.save(restaurant2);
	//
	// 	User customer2 = new User(UserRoleEnum.USER, "이름", "aaa2346246@gmail.com", "customerId333", "pw12", "qqqq",
	// 		"01033333333");
	// 	userRepository.save(customer2);
	//
	// 	lineupService.startWaiting(
	// 		new StartWaitingServiceRequest(customer2, restaurant2.getId(), 5, LocalDateTime.now()));
	//
	// 	Lineup lineup = lineupRepository.findAllByUserId(customer1.getId()).get(0);
	// 	lineupService.cancelWaiting(new CancelWaitingRequest(lineup.getId(), lineup.getUserId()));
	//
	// 	Lineup canceledLineup = lineupRepository.findAll().get(0);
	//
	// 	assertThat(lineup.getWaitingNumber()).isEqualTo(1);
	// 	assertThat(lineup.getNumOfMembers()).isEqualTo(5);
	// 	assertThat(lineup.getCallCount()).isEqualTo(0);
	// 	assertThat(canceledLineup.getStatus()).isEqualTo(ArrivalStatusEnum.CANCEL);
	//
	// 	Lineup lineup2 = lineupRepository.findAllByUserId(customer2.getId()).get(0);
	// 	assertThatThrownBy(
	// 		() -> lineupService.cancelWaiting(new CancelWaitingRequest(lineup.getId(), lineup2.getUserId())))
	// 		.isInstanceOf(IllegalArgumentException.class)
	// 		.hasMessage("유저 정보가 일치하지 않습니다.");
	// }
	//
	// @Test
	// @DisplayName("손님 호출")
	// void callCustomer() {
	// 	User customer = userRepository.findByUsernameAndIsDeletedFalse("customerId").get();
	// 	Long sellerId = userRepository.findByUsernameAndIsDeletedFalse("sellerId").get().getId();
	// 	Restaurant restaurant = restaurantRepository.findByUserId(sellerId).get();
	// 	lineupService.startWaiting(
	// 		new StartWaitingServiceRequest(customer, restaurant.getId(), 5, LocalDateTime.now()));
	//
	// 	Lineup lineup = lineupRepository.findAll().get(0);
	// 	UpdateArrivalStatusServiceRequest callRequest = new UpdateArrivalStatusServiceRequest(sellerId, lineup.getId(),
	// 		ArrivalStatusEnum.CALL);
	//
	// 	lineupService.updateArrivalStatus(callRequest);
	// 	assertThat(lineup.getCallCount()).isEqualTo(1);
	//
	// 	lineupService.updateArrivalStatus(callRequest);
	// 	assertThat(lineup.getCallCount()).isEqualTo(2);
	//
	// 	assertThatThrownBy(() -> lineupService.updateArrivalStatus(callRequest))
	// 		.isInstanceOf(IllegalArgumentException.class)
	// 		.hasMessage("호출은 최대 2번까지 가능합니다.");
	// }
	//
	// @Test
	// @DisplayName("손님 취소")
	// void cancelCustomer() {
	// 	User customer = userRepository.findByUsernameAndIsDeletedFalse("customerId").get();
	// 	Long sellerId = userRepository.findByUsernameAndIsDeletedFalse("sellerId").get().getId();
	// 	Restaurant restaurant = restaurantRepository.findByUserId(sellerId).get();
	// 	lineupService.startWaiting(
	// 		new StartWaitingServiceRequest(customer, restaurant.getId(), 5, LocalDateTime.now()));
	//
	// 	Lineup lineup = lineupRepository.findAll().get(0);
	// 	UpdateArrivalStatusServiceRequest cancelRequest = new UpdateArrivalStatusServiceRequest(sellerId,
	// 		lineup.getId(),
	// 		ArrivalStatusEnum.CANCEL);
	//
	// 	lineupService.updateArrivalStatus(cancelRequest);
	// 	assertThat(lineup.getStatus()).isEqualTo(ArrivalStatusEnum.CANCEL);
	// }
	//
	// @Test
	// @DisplayName("오늘 레스토랑의 줄서기 조회")
	// void getTodayLineups() {
	// 	Long sellerId = userRepository.findByUsernameAndIsDeletedFalse("sellerId").get().getId();
	// 	Restaurant restaurant = restaurantRepository.findByUserId(sellerId).get();
	//
	// 	for (int i = 0; i < 10; i++) {
	// 		User customer = new User(UserRoleEnum.USER, "이름" + i, i + "@gmail.com", "customerId" + i, "pw" + i,
	// 			"sj" + i, "0101212121" + i);
	// 		userRepository.save(customer);
	// 		lineupService.startWaiting(
	// 			new StartWaitingServiceRequest(customer, restaurant.getId(), i + 1, LocalDateTime.now()));
	// 	}
	//
	// 	for (int i = 10; i < 15; i++) {
	// 		User customer = new User(UserRoleEnum.USER, "이름" + i, i + "@gmail.com", "customerId" + i, "pw" + i,
	// 			"sj" + i, "0101212121" + i);
	// 		userRepository.save(customer);
	// 	}
	//
	// 	List<TodayLineupResponse> todayLineups = lineupService.getTodayLineups(sellerId);
	//
	// 	assertThat(todayLineups.size()).isEqualTo(10);
	// }
	//
	// @Test
	// @DisplayName("줄서기 히스토리 조회")
	// void getLineupHistories() {
	// 	User customer = userRepository.findByUsernameAndIsDeletedFalse("customerId").get();
	//
	// 	for (int i = 0; i < 10; i++) {
	// 		User seller = new User(UserRoleEnum.SELLER, "사장" + i, i + "@naver.com", "sellerId" + i, "pw" + i,
	// 			"seller" + i, "0101111111" + i);
	// 		userRepository.save(seller);
	//
	// 		Restaurant restaurant = new Restaurant(
	// 			new SaveDummyRestaurantRequest("레스토랑" + i, new Address("서울시", "강남구", "강남대로"), new Position(0.0, 0.0),
	// 				"0100000000" + i, "일식>스시>오마카세", seller));
	// 		restaurantRepository.save(restaurant);
	//
	// 		lineupService.startWaiting(
	// 			new StartWaitingServiceRequest(customer, restaurant.getId(), i + 1, LocalDateTime.now()));
	// 	}
	//
	// 	List<LineupRecordWithTypeResponse> lineupRecords = lineupService.getLineupRecords(
	// 		new GetLineupRecordsServiceRequest(customer.getId(), null));
	//
	// 	assertThat(lineupRecords.size()).isEqualTo(10);
	// }

	@Test
	@Commit
	@Order(2)
	void concurrentWaiting() throws InterruptedException {
		final int NUM_OF_TASKS = 3;

		Long sellerId = userRepository.findByUsernameAndIsDeletedFalse("sellerId").get().getId();
		Restaurant restaurant = restaurantRepository.findByUserId(sellerId).get();

		ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_TASKS);
		CountDownLatch countDownLatch = new CountDownLatch(NUM_OF_TASKS);

		for (int i = 0; i < NUM_OF_TASKS; i++) {
			User user = userRepository.findById((long)(i + 1)).get();
			executorService.submit(() -> {
				try {
					lineupService.startWaiting(
						new StartWaitingServiceRequest(user, restaurant.getId(), 5, LocalDateTime.now()));
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();

		try {
			if (!executorService.awaitTermination(300, TimeUnit.MILLISECONDS)) {
				executorService.shutdownNow();
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
		}

		assertThat(lineupRepository.findLastWaitingNumberByRestaurantId(restaurant.getId())).isEqualTo(10);
	}
}