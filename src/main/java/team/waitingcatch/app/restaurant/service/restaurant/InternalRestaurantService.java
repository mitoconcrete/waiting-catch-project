package team.waitingcatch.app.restaurant.service.restaurant;

import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.RestaurantInfo;

public interface InternalRestaurantService {
	Restaurant _getRestaurantById(Long id);

	Restaurant _getRestaurantByUserId(Long userId);

	RestaurantInfo _getRestaurantInfoByUserId(Long userId);

	RestaurantInfo _getRestaurantInfoWithRestaurantByRestaurantId(Long id);

	RestaurantInfo _getRestaurantInfoWithRestaurantByUserId(Long userId);

	void _createRestaurant(ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest request);

	void _openLineup(Long restaurantId);

	void _closeLineup(Long restaurantId);

	Restaurant _deleteRestaurantBySellerId(Long sellerId);
}