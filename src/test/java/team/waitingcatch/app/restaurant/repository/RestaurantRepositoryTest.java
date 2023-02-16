package team.waitingcatch.app.restaurant.repository;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestaurantRepositoryTest {
	@Autowired
	RestaurantRepository restaurantRepository;

	@Test
	void findRestaurantsBySearchKeywordsContains() {
		restaurantRepository.findRestaurantsBySearchKeywordsContaining(any(String.class));
	}

	// @Test
	// void findRestaurantsByDistance() {
	// 	restaurantRepository.findRestaurantsByDistance(1.0, 1.0);
	// }

}