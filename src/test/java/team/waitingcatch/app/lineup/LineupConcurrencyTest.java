package team.waitingcatch.app.lineup;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.lineup.dto.StartWaitingServiceRequest;
import team.waitingcatch.app.lineup.entity.WaitingNumber;
import team.waitingcatch.app.lineup.repository.LineupRepository;
import team.waitingcatch.app.lineup.repository.WaitingNumberRepository;
import team.waitingcatch.app.lineup.service.LineupService;
import team.waitingcatch.app.restaurant.dto.restaurant.SaveDummyRestaurantRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.RestaurantInfo;
import team.waitingcatch.app.restaurant.repository.RestaurantInfoRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.enums.UserRoleEnum;
import team.waitingcatch.app.user.repository.UserRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LineupConcurrencyTest {
	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	@Autowired
	UserRepository userRepository;

	@Autowired
	RestaurantRepository restaurantRepository;

	@Autowired
	RestaurantInfoRepository restaurantInfoRepository;

	@Autowired
	LineupRepository lineupRepository;

	@Autowired
	WaitingNumberRepository waitingNumberRepository;

	@Autowired
	LineupService lineupService;

	private final int NUM_OF_TASKS = 10;

	@Test
	@DisplayName("더미 데이터 저장")
	@Commit
	@Order(1)
	void insertDummyDataBeforeConcurrentWaitingTest() {
		List<String> searchKeywords = List.of("korean", "japan");

		User seller1 = new User(UserRoleEnum.SELLER, "판매자1", "abcdef@gmail.com", "sellerIdA", "pw123", "sjsjA",
			"01012301230");
		userRepository.save(seller1);

		User seller2 = new User(UserRoleEnum.SELLER, "판매자2", "ghijk@gmail.com", "sellerIdB", "pw123", "sjsjB",
			"01012001200");
		userRepository.save(seller2);

		Restaurant restaurant1 = new Restaurant(
			new SaveDummyRestaurantRequest("맛집1", "12345", "서울시 강남구 강남대로", "1", new Position(0.0, 0.0),
				"01000000001", "일식>스시>오마카세", seller1, searchKeywords));
		restaurantRepository.save(restaurant1);
		openRestaurant(restaurant1);

		Restaurant restaurant2 = new Restaurant(
			new SaveDummyRestaurantRequest("레스토랑2", "12345", "서울시 강남구 강남대로", "1", new Position(0.0, 0.0),
				"01000000002", "일식>스시>오마카세", seller2, searchKeywords));
		restaurantRepository.save(restaurant2);
		openRestaurant(restaurant2);

		for (int i = 0; i < NUM_OF_TASKS; i++) {
			User customer = new User(UserRoleEnum.USER, "이름" + i, i + "@gmail.com", "customerId" + i, "pw" + i,
				"sj" + i, "0101212121" + i);
			userRepository.save(customer);
		}
	}

	@Test
	@DisplayName("줄서기 동시성 테스트")
	@Commit
	@Order(2)
	void concurrentWaiting() throws InterruptedException {
		Long sellerId1 = userRepository.findByUsernameAndIsDeletedFalse("sellerIdA").get().getId();
		Long sellerId2 = userRepository.findByUsernameAndIsDeletedFalse("sellerIdB").get().getId();
		Restaurant restaurant1 = restaurantRepository.findByUserId(sellerId1).get();
		Restaurant restaurant2 = restaurantRepository.findByUserId(sellerId2).get();

		ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_TASKS);
		CountDownLatch countDownLatch = new CountDownLatch(NUM_OF_TASKS);

		for (int i = 0; i < NUM_OF_TASKS; i++) {
			User user = userRepository.findById((long)(i + 1)).get();
			executorService.submit(() -> {
				try {
					lineupService.startWaiting(
						new StartWaitingServiceRequest(user, restaurant1.getId(), 0.0, 0.0, 5, LocalDateTime.now()));
					lineupService.startWaiting(
						new StartWaitingServiceRequest(user, restaurant2.getId(), 0.0, 0.0, 5, LocalDateTime.now()));
				} catch (Exception e) {
					e.printStackTrace();
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
	}

	@Test
	@DisplayName("결과 검증")
	@Order(3)
	void assertResult() {
		Long sellerId1 = userRepository.findByUsernameAndIsDeletedFalse("sellerIdA").get().getId();
		Long sellerId2 = userRepository.findByUsernameAndIsDeletedFalse("sellerIdB").get().getId();
		Long restaurantId1 = restaurantRepository.findByUserId(sellerId1).get().getId();
		Long restaurantId2 = restaurantRepository.findByUserId(sellerId2).get().getId();

		assertThat((int)lineupRepository.findByRestaurantId(restaurantId1).stream().distinct().count())
			.isGreaterThan(2);
		assertThat((int)lineupRepository.findByRestaurantId(restaurantId2).stream().distinct().count())
			.isGreaterThan(2);
	}

	private void openRestaurant(Restaurant restaurant) {
		RestaurantInfo restaurantInfo = new RestaurantInfo(restaurant);
		restaurantInfo.openLineup();
		restaurantInfoRepository.save(restaurantInfo);

		WaitingNumber waitingNumber2 = WaitingNumber.of(restaurant);
		waitingNumberRepository.save(waitingNumber2);
	}
}