package team.waitingcatch.app.lineup;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.lineup.dto.CancelWaitingRequest;
import team.waitingcatch.app.lineup.dto.GetLineupRecordsServiceRequest;
import team.waitingcatch.app.lineup.dto.LineupRecordWithTypeResponse;
import team.waitingcatch.app.lineup.dto.StartWaitingServiceRequest;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;
import team.waitingcatch.app.lineup.dto.UpdateArrivalStatusServiceRequest;
import team.waitingcatch.app.lineup.entity.Lineup;
import team.waitingcatch.app.lineup.entity.WaitingNumber;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;
import team.waitingcatch.app.lineup.repository.LineupHistoryRepository;
import team.waitingcatch.app.lineup.repository.LineupRepository;
import team.waitingcatch.app.lineup.repository.WaitingNumberRepository;
import team.waitingcatch.app.lineup.service.LineupService;
import team.waitingcatch.app.restaurant.dto.restaurant.SaveDummyRestaurantRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.RestaurantInfo;
import team.waitingcatch.app.restaurant.repository.RestaurantInfoRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.restaurant.service.requestseller.SellerManagementService;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.enums.UserRoleEnum;
import team.waitingcatch.app.user.repository.UserRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LineupIntegrationTest {
	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	@Autowired
	LineupRepository lineupRepository;

	@Autowired
	LineupHistoryRepository lineupHistoryRepository;

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

	@Autowired
	SellerManagementService sellerManagementService;

	@BeforeEach
	public void beforeEach() {
		List<String> searchKeywords = List.of("korean", "japan");

		User customer = new User(UserRoleEnum.USER, "??????1", "aaa@gmail.com", "customerId", "pw12", "sj",
			"01012341234");
		userRepository.save(customer);

		User seller = new User(UserRoleEnum.SELLER, "?????????1", "bbb@gmail.com", "sellerId", "pw123", "sjsj",
			"01012312312");
		userRepository.save(seller);

		Restaurant restaurant = new Restaurant(
			new SaveDummyRestaurantRequest("????????????1", "12345", "????????? ????????? ????????????", "1", new Position(0.0, 0.0),
				"01000000000", "??????>??????>????????????", seller, searchKeywords));
		openRestaurant(restaurant);
	}

	@Test
	@DisplayName("????????? ??????")
	void startWaiting() {
		User customer = userRepository.findByUsernameAndIsDeletedFalse("customerId").get();
		Long sellerId = userRepository.findByUsernameAndIsDeletedFalse("sellerId").get().getId();
		Restaurant restaurant = restaurantRepository.findByUserId(sellerId).get();
		lineupService.startWaiting(
			new StartWaitingServiceRequest(customer, restaurant.getId(), 0.0, 0.0, 5, LocalDateTime.now()));

		Lineup lineup = lineupRepository.findAll().get(0);

		assertThat(lineup.getWaitingNumber()).isEqualTo(1);
		assertThat(lineup.getNumOfMembers()).isEqualTo(5);
		assertThat(lineup.getCallCount()).isEqualTo(0);
		assertThat(lineup.getStatus()).isEqualTo(ArrivalStatusEnum.WAIT);
	}

	@Test
	@DisplayName("????????? ??????")
	void cancelWaiting() {
		User customer1 = userRepository.findByUsernameAndIsDeletedFalse("customerId").get();
		Long sellerId1 = userRepository.findByUsernameAndIsDeletedFalse("sellerId").get().getId();
		Restaurant restaurant1 = restaurantRepository.findByUserId(sellerId1).get();
		lineupService.startWaiting(
			new StartWaitingServiceRequest(customer1, restaurant1.getId(), 0.0, 0.0, 5, LocalDateTime.now()));

		Lineup lineup = lineupRepository.findAllByUserId(customer1.getId()).get(0);

		assertThat(lineup.getWaitingNumber()).isEqualTo(1);
		assertThat(lineup.getNumOfMembers()).isEqualTo(5);
		assertThat(lineup.getCallCount()).isEqualTo(0);
		assertThat(lineup.getStatus()).isEqualTo(ArrivalStatusEnum.WAIT);

		lineupService.cancelWaiting(new CancelWaitingRequest(lineup.getId(), lineup.getUserId()));

		assertThat(lineup.getStatus()).isEqualTo(ArrivalStatusEnum.CANCEL);
	}

	@Test
	@DisplayName("?????? ??????")
	void sellerCallCustomer() {
		User customer = userRepository.findByUsernameAndIsDeletedFalse("customerId").get();
		Long sellerId = userRepository.findByUsernameAndIsDeletedFalse("sellerId").get().getId();
		Restaurant restaurant = restaurantRepository.findByUserId(sellerId).get();
		lineupService.startWaiting(
			new StartWaitingServiceRequest(customer, restaurant.getId(), 0.0, 0.0, 5, LocalDateTime.now()));

		Lineup lineup = lineupRepository.findAllByUserId(customer.getId()).get(0);
		UpdateArrivalStatusServiceRequest callRequest = new UpdateArrivalStatusServiceRequest(sellerId, lineup.getId(),
			ArrivalStatusEnum.CALL);

		assertThatThrownBy(() -> lineupService.updateArrivalStatus(callRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Empty key");
	}

	@Test
	@DisplayName("?????? ??????")
	void sellerCancelCustomer() {
		User customer = userRepository.findByUsernameAndIsDeletedFalse("customerId").get();
		Long sellerId = userRepository.findByUsernameAndIsDeletedFalse("sellerId").get().getId();
		Restaurant restaurant = restaurantRepository.findByUserId(sellerId).get();
		lineupService.startWaiting(
			new StartWaitingServiceRequest(customer, restaurant.getId(), 0.0, 0.0, 5, LocalDateTime.now()));

		Lineup lineup = lineupRepository.findAllByUserId(customer.getId()).get(0);
		UpdateArrivalStatusServiceRequest cancelRequest = new UpdateArrivalStatusServiceRequest(sellerId,
			lineup.getId(),
			ArrivalStatusEnum.CANCEL);

		assertThatThrownBy(() -> lineupService.updateArrivalStatus(cancelRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Empty key");
	}

	@Test
	@DisplayName("?????? ??????????????? ????????? ??????")
	void getTodayLineups() {
		Long sellerId = userRepository.findByUsernameAndIsDeletedFalse("sellerId").get().getId();
		Restaurant restaurant = restaurantRepository.findByUserId(sellerId).get();

		for (int i = 0; i < 10; i++) {
			User customer = new User(UserRoleEnum.USER, "??????" + i, i + "@naver.com", "testId" + i, "pw" + i,
				"test" + i, "0102233223" + i);
			userRepository.save(customer);
			lineupService.startWaiting(
				new StartWaitingServiceRequest(customer, restaurant.getId(), 0.0, 0.0, i + 1, LocalDateTime.now()));
		}

		for (int i = 10; i < 15; i++) {
			User customer = new User(UserRoleEnum.USER, "??????" + i, i + "@naver.com", "testId" + i, "pw" + i,
				"test" + i, "010223322" + i);
			userRepository.save(customer);
		}

		List<TodayLineupResponse> todayLineups = lineupService.getTodayLineups(sellerId);

		assertThat(todayLineups.size()).isEqualTo(10);
	}

	@Test
	@DisplayName("????????? ???????????? ??????")
	void getLineupHistories() {
		User customer = userRepository.findByUsernameAndIsDeletedFalse("customerId").get();
		List<String> searchKeywords = List.of("korean", "japan");

		for (int i = 0; i < 3; i++) {
			User seller = new User(UserRoleEnum.SELLER, "??????" + i, i + "@naver.com", "sellerId" + i, "pw" + i,
				"seller" + i, "0101111111" + i);
			userRepository.save(seller);

			Restaurant restaurant = new Restaurant(
				new SaveDummyRestaurantRequest("????????????" + i, "12345", "????????? ????????? ????????????", "1", new Position(0.0, 0.0),
					"0100000011" + i, "??????>??????>????????????", seller, searchKeywords));

			openRestaurant(restaurant);

			lineupService.startWaiting(
				new StartWaitingServiceRequest(customer, restaurant.getId(), 0.0, 0.0, i + 1, LocalDateTime.now()));
		}

		List<LineupRecordWithTypeResponse> lineupRecords = lineupService.getLineupRecords(
			new GetLineupRecordsServiceRequest(customer.getId(), null));

		assertThat(lineupRecords.size()).isEqualTo(3);
	}

	private void openRestaurant(Restaurant restaurant) {
		restaurantRepository.save(restaurant);

		RestaurantInfo restaurantInfo = new RestaurantInfo(restaurant);
		restaurantInfo.openLineup();
		restaurantInfoRepository.save(restaurantInfo);

		waitingNumberRepository.save(WaitingNumber.of(restaurant));
	}
}