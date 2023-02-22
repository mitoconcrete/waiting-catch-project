package team.waitingcatch.app.lineup;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.lineup.dto.CreateReviewEntityRequest;
import team.waitingcatch.app.lineup.entity.Review;
import team.waitingcatch.app.lineup.repository.ReviewRepository;
import team.waitingcatch.app.restaurant.dto.restaurant.SaveDummyRestaurantRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.enums.UserRoleEnum;
import team.waitingcatch.app.user.repository.UserRepository;

@SpringBootTest
@Commit
class ReviewIntegrationTest {
	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	@Autowired
	UserRepository userRepository;

	@Autowired
	RestaurantRepository restaurantRepository;

	@Autowired
	ReviewRepository reviewRepository;

	@BeforeEach
	void beforeEach() {
		User customer = new User(UserRoleEnum.USER, "이름1", "aaa@gmail.com", "customerId123", "pw12", "sj",
			"01012341234");
		userRepository.save(customer);
		User seller = new User(UserRoleEnum.SELLER, "이름2", "bbb@gmail.com", "sellerId123", "pw123", "sjsj",
			"01012312312");
		userRepository.save(seller);

		Restaurant restaurant = new Restaurant(
			new SaveDummyRestaurantRequest("레스토랑1", new Address("서울시", "강남구", "강남대로"), new Position(0.0, 0.0),
				"01000000000", "일식>스시>오마카세", seller));
		restaurantRepository.save(restaurant);
	}

	@Test
	void saveReview() {
		User customer = userRepository.findByUsernameAndIsDeletedFalse("customerId1").get();
		Long sellerId = userRepository.findByUsernameAndIsDeletedFalse("sellerId1").get().getId();
		Restaurant restaurant = restaurantRepository.findByUserId(sellerId).get();

		Review review = Review.craeteReview(
			new CreateReviewEntityRequest(customer, restaurant, 0, " ", new ArrayList<>()));
		reviewRepository.save(review);
	}
}