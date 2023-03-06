package team.waitingcatch.app.event.service.event;

import java.util.List;

import team.waitingcatch.app.event.dto.event.GetEventsResponse;
import team.waitingcatch.app.event.entity.Event;

public interface InternalEventService {
	Event _getEventById(Long id);

	List<GetEventsResponse> _getEventsResponse(List<Event> events);

	void _bulkSoftDeleteByRestaurantId(Long restaurantId);
}