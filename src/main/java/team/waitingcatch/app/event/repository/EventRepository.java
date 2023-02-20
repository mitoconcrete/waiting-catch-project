package team.waitingcatch.app.event.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface EventRepository extends JpaRepository<Event, Long> {

	Optional<Event> findByIdAndRestaurant(Long id, Restaurant restaurant);

	List<Event> findByRestaurantIsNullAndIsDeletedFalse();

	List<Event> findByRestaurantAndIsDeletedFalse(Restaurant restaurant);

	Optional<Event> findByIdAndIsDeletedFalse(Long id);

	Optional<Event> findByIdAndRestaurantAndIsDeletedFalse(Long id, Restaurant restaurant);

	@Modifying
	@Query("update Event e set e.isDeleted = true where e.restaurant.id = :restaurantId")
	void softDeleteByRestaurantId(@Param("restaurantId") Long restaurantId);
}
