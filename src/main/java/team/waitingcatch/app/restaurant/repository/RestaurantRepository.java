package team.waitingcatch.app.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantJpaResponse;
import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	Optional<Restaurant> findByUserId(Long userId);

	Restaurant findByUser_Username(String sellerName);

	@Query(value =
		"select new team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantJpaResponse("
			+ "r.name, r.images, i.rate, r.searchKeywords, r.position, i.currentWaitingNumber) from Restaurant r "
			+ "join fetch RestaurantInfo i on r.id = i.restaurantId "
			+ "where r.searchKeywords like %:keyword%")
	List<SearchRestaurantJpaResponse> findRestaurantsBySearchKeywordsContaining(String keyword);

	@Query(value = "select r from Restaurant r where "
		+ "(6371 * acos(cos(radians(:latitude)) * cos(radians(r.position.latitude)) "
		+ "* cos(radians(r.position.longitude) - radians(:longitude)) + sin(radians(:latitude)) "
		+ "* sin(radians(r.position.latitude)))) < 3")
	List<Restaurant> findRestaurantsByDistance(
		@Param("latitude") double latitude, @Param("longitude") double longitude);

	// @Query("select r, "
	// 	+ "ST_Distance_Sphere(Point(:latitude, :longitude), Point(r.position.latitude, r.position.longitude)) "
	// 	+ "as distance from Restaurant r where distance < 3000")
	// List<Restaurant> findRestaurantsByDistance(
	// 	@Param("latitude") double latitude, @Param("longitude") double longitude);

}
