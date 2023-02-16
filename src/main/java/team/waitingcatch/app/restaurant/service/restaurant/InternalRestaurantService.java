package team.waitingcatch.app.restaurant.service.restaurant;

import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface InternalRestaurantService {
	Restaurant _getRestaurant(Long restaurantId);

	Restaurant _getRestaurantByUserId(Long userId);
}
