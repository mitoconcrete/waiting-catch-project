package team.waitingcatch.app.restaurant.repository.querydsl;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithin3kmRadiusJpaResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantJpaResponse;

public interface RestaurantInfoRepositoryCustom {
	List<SearchRestaurantJpaResponse> findRestaurantsBySearchKeywordsContaining(String keyword);

	List<RestaurantsWithin3kmRadiusJpaResponse> findRestaurantsByDistance(double latitude, double longitude);
}
