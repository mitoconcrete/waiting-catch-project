package team.waitingcatch.app.event.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface EventServiceRepository extends JpaRepository<Event, Long> {

	Optional<Event> findByIdAndRestaurant(Long id, Restaurant restaurant);

	List<Event> findByRestaurantIsNullAndIsDeletedFalse();

	List<Event> findByRestaurantAndIsDeletedFalse(Restaurant restaurant);

	Optional<Event> findByIdAndIsDeletedFalse(Long id);

}
