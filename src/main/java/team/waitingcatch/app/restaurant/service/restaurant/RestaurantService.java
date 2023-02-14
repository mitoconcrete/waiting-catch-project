package team.waitingcatch.app.restaurant.service.restaurant;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.RestaurantBasicInfoResponse;
import team.waitingcatch.app.restaurant.dto.RestaurantResponse;

public interface RestaurantService {
	List<RestaurantResponse> getRestaurants();

	RestaurantBasicInfoResponse getRestaurantBasicInfo(Long restaurantId);
}
