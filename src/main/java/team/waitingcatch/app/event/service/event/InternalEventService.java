package team.waitingcatch.app.event.service.event;

import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface InternalEventService {

	public Event _getEventFindById(Long id);

	public Restaurant _getRestaurantFindByUsername(String name);

	public Restaurant _getRestaurantFindById(Long id);

}
