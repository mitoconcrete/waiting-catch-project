package team.waitingcatch.app.restaurant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	// @Query(value = "select r from Restaurant r join fetch User u where u.username = :name")
	// Optional<Restaurant> findByUsername(@Param("name") String name);

	Optional<Restaurant> findByUserId(Long userId);

	// Optional<Restaurant> findByName(String name);
}
