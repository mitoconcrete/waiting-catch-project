package team.waitingcatch.app.restaurant.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	@Query("select r from Restaurant r where r.user.id = :userId")
	Optional<Restaurant> findByUserId(@Param("userId") Long userId);

	Page<Restaurant> findByNameContaining(String searchVal, Pageable pageable);

	Restaurant findByUser(User user);
}