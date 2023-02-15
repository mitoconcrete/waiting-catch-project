package team.waitingcatch.app.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.restaurant.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
	Optional<List<Menu>> findAllByRestaurantId(Long restaurantId);
}
