package team.waitingcatch.app.restaurant.repository;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithinRadiusJpaResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantJpaResponse;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RestaurantInfoRepositoryTest {
	@Autowired
	private RestaurantInfoRepository restaurantInfoRepository;

	@Test
	void findRestaurantsBySearchKeywordsContains() {
		Pageable pageable = mock(Pageable.class);

		when(pageable.getPageSize()).thenReturn(5);

		Slice<SearchRestaurantJpaResponse> jpaResponses =
			restaurantInfoRepository.findRestaurantsBySearchKeywordsContaining(
				null,
				"keywords",
				pageable
			);

		// assertEquals(5, jpaResponses.size());
	}

	@Test
	void findRestaurantsByDistance() {
		Pageable pageable = mock(Pageable.class);

		when(pageable.getPageSize()).thenReturn(5);

		Slice<RestaurantsWithinRadiusJpaResponse> restaurants =
			restaurantInfoRepository.findRestaurantsByDistance(
				null,
				37.339141,
				127.082427,
				3,
				pageable
			);

		// assertEquals(3, restaurants.size());
	}

}