package team.waitingcatch.app.restaurant.repository.querydsl;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithinRadiusJpaResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantJpaResponse;

public interface RestaurantInfoRepositoryCustom {
	List<SearchRestaurantJpaResponse> findRestaurantsBySearchKeywordsContaining(String keyword);

	List<RestaurantsWithinRadiusJpaResponse> findRestaurantsByDistance(double latitude, double longitude,
		int distance);
}
