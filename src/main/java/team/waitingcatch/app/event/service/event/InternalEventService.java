package team.waitingcatch.app.event.service.event;

import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface InternalEventService {

	Event _getEventById(Long id);

	Restaurant _getRestaurantById(Long id);

	void _bulkSoftDeleteByRestaurantId(Long restaurantId);
}
