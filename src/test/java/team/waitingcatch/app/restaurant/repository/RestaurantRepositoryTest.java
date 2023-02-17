package team.waitingcatch.app.restaurant.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantJpaResponse;
import team.waitingcatch.app.restaurant.entity.Restaurant;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RestaurantRepositoryTest {
	@Autowired
	RestaurantRepository restaurantRepository;

	@Test
	void findRestaurantsBySearchKeywordsContains() {
		List<SearchRestaurantJpaResponse> jpaResponses =
			restaurantRepository.findRestaurantsBySearchKeywordsContaining("keywords");

		assertEquals(5, jpaResponses.size());
	}

	@Test
	void findRestaurantsByDistance() {

		List<Restaurant> restaurants =
			restaurantRepository.findRestaurantsByDistance(37.339141, 127.082427);

		assertEquals(3, restaurants.size());

	}

}