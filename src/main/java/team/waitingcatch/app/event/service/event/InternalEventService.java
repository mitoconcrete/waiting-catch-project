package team.waitingcatch.app.event.service.event;

import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface InternalEventService {

	public Event _getEventById(Long id);

	public Restaurant _getRestaurantByUsername(String name);

	public Restaurant _getRestaurantById(Long id);

}
