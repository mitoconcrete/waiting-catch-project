package team.waitingcatch.app.restaurant.service.restaurant;

import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface InternalRestaurantService {
	Restaurant _getById(Long restaurantId);

	Restaurant _getRestaurantByUserId(Long userId);

	void createRestaurant(ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest request);

	void _openLineup(Long restaurantId);

	void _closeLineup(Long restaurantId);

	Restaurant _deleteRestaurantBySellerId(Long sellerId);
}