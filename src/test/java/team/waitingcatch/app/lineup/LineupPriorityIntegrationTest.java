package team.waitingcatch.app.lineup;

import static org.assertj.core.api.Assertions.*;
import static team.waitingcatch.app.exception.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.exception.IllegalRequestException;
import team.waitingcatch.app.lineup.dto.CancelWaitingRequest;
import team.waitingcatch.app.lineup.dto.GetLineupPriorityServiceRequest;
import team.waitingcatch.app.lineup.dto.StartWaitingServiceRequest;
import team.waitingcatch.app.lineup.entity.Lineup;
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
class LineupPriorityIntegrationTest {
	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	@Autowired
	LineupRepository lineupRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RestaurantRepository restaurantRepository;

	@Autowired
	RestaurantInfoRepository restaurantInfoRepository;

	@Autowired
	WaitingNumberRepository waitingNumberRepository;

	@Autowired
	LineupService lineupService;

	@BeforeEach
	public void beforeEach() {
		List<String> searchKeywords = List.of("korean", "japan");

		User customer = new User(UserRoleEnum.USER, "유저1", "aaa@gmail.com", "customerId", "pw12", "sj",
			"01012341234");
		userRepository.save(customer);

		User seller = new User(UserRoleEnum.SELLER, "판매자1", "bbb@gmail.com", "sellerId", "pw123", "sjsj",
			"01012312312");
		userRepository.save(seller);

		Restaurant restaurant = new Restaurant(
			new SaveDummyRestaurantRequest("레스토랑1", "12345", "서울시 강남구 강남대로", "1", new Position(0.0, 0.0),
				"01000000000", "일식>스시>오마카세", seller, searchKeywords));
		openRestaurant(restaurant);
	}

	@Test
	@DisplayName("내 앞 대기인원 수 확인")
	void lineupPriority() {
		Long sellerId = userRepository.findByUsernameAndIsDeletedFalse("sellerId").get().getId();
		Long restaurantId = restaurantRepository.findByUserId(sellerId).get().getId();

		final int numOfCustomer = 10;
		List<Long> customerIdList = new ArrayList<>(numOfCustomer);

		for (int i = 0; i < numOfCustomer; i++) {
			User customer = new User(UserRoleEnum.USER, "이름" + i, i + "@naver.com", "testId" + i, "pw" + i,
				"test" + i, "0102233223" + i);
			customerIdList.add(userRepository.save(customer).getId());
			lineupService.startWaiting(
				new StartWaitingServiceRequest(customer, restaurantId, 0.0, 0.0, i + 1, LocalDateTime.now()));

			if (i == 0 || i == 5) {
				Lineup lineup = lineupRepository.findAllByUserId(customer.getId()).get(0);
				lineupService.cancelWaiting(new CancelWaitingRequest(lineup.getId(), lineup.getUserId()));
			}
		}

		int priority = 0;
		for (int i = 0; i < numOfCustomer; i++) {
			final long customerId = customerIdList.get(i);

			if (i == 0 || i == 5) {
				assertThatThrownBy(
					() -> lineupService.getPriority(
						new GetLineupPriorityServiceRequest(restaurantId, customerId)))
					.isInstanceOf(IllegalRequestException.class)
					.hasMessage(ILLEGAL_ACCESS.getMessage());
			} else {
				assertThat(lineupService.getPriority(new GetLineupPriorityServiceRequest(restaurantId, customerId)))
					.isEqualTo(priority++);
			}
		}
	}

	private void openRestaurant(Restaurant restaurant) {
		restaurantRepository.save(restaurant);

		RestaurantInfo restaurantInfo = new RestaurantInfo(restaurant);
		restaurantInfo.openLineup();
		restaurantInfoRepository.save(restaurantInfo);

		waitingNumberRepository.save(WaitingNumber.of(restaurant));
	}
}