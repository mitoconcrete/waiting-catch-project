package team.waitingcatch.app.event.service.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import team.waitingcatch.app.event.dto.event.GetEventsResponse;
import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface InternalEventService {
	Event _getEventById(Long id);

	Page<GetEventsResponse> _getEventsResponse(Page<Event> events, Restaurant restaurant, Pageable pageable);

	void _bulkSoftDeleteByRestaurantId(Long restaurantId);
}
