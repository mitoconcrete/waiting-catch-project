package team.waitingcatch.app.restaurant.service.restaurant;

import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface InternalRestaurantService {
	Restaurant _getRestaurant(Long restaurantId);

	// Restaurant _getRestaurantFindByUsername(String name);

	Restaurant _getRestaurantByUserId(Long userId);

	void _openLineup(Long restaurantId);

	void _closeLineup(Long restaurantId);

	void _deleteRestaurantBySellerId(Long sellerId);
	
}