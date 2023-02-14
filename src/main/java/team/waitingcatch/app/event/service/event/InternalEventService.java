package team.waitingcatch.app.event.service.event;

import java.util.Optional;

import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface InternalEventService {

	public Optional<Event> _getEventFindByName(String name);

	public Event _getEventFindById(Long id);

	public Restaurant _getRestaurantFindByUsername(String name);
}
