package team.waitingcatch.app.restaurant.repository.querydsl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithinRadiusJpaResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantJpaResponse;

public interface RestaurantInfoRepositoryCustom {
	Slice<SearchRestaurantJpaResponse> findRestaurantsBySearchKeywordsContaining(Long id, String keyword,
		Pageable pageable);

	// Slice<RestaurantsWithinRadiusJpaResponse> findRestaurantsByDistance(Long id, double latitude, double longitude,
	// 	int distance, Pageable pageable);

	Slice<RestaurantsWithinRadiusJpaResponse> findRestaurantsByLatitudeAndLongitude(
		Double lastDistance, double latitude, double longitude, double maxLatitude, double maxLongitude,
		double minLatitude, double minLongitude, Pageable pageable);
}
