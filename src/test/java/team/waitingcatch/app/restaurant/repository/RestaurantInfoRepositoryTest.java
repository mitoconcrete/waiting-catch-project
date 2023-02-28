package team.waitingcatch.app.restaurant.repository;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithinRadiusJpaResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantJpaResponse;

@SpringBootTest
class RestaurantInfoRepositoryTest {
	@Autowired
	private RestaurantInfoRepository restaurantInfoRepository;

	@Test
	void findRestaurantsBySearchKeywordsContains() {
		List<SearchRestaurantJpaResponse> jpaResponses =
			restaurantInfoRepository.findRestaurantsBySearchKeywordsContaining("keywords");

		// assertEquals(5, jpaResponses.size());
	}

	@Test
	void findRestaurantsByDistance() {
		List<RestaurantsWithinRadiusJpaResponse> restaurants =
			restaurantInfoRepository.findRestaurantsByDistance(37.339141, 127.082427, 3);

		// assertEquals(3, restaurants.size());
	}

}
