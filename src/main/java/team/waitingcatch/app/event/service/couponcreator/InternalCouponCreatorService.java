package team.waitingcatch.app.event.service.couponcreator;

import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface InternalCouponCreatorService {

	public Event _getEventFindById(Long id);

	public Restaurant _getRestaurantFindByUsername(String name);
}
