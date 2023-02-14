package team.waitingcatch.app.restaurant.service.restaurant;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.RestaurantBasicInfoResponse;
import team.waitingcatch.app.restaurant.dto.RestaurantBasicInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.RestaurantDetailedInfoResponse;
import team.waitingcatch.app.restaurant.dto.RestaurantDetailedInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.RestaurantResponse;

public interface RestaurantService {
	RestaurantBasicInfoResponse getRestaurantBasicInfo(RestaurantBasicInfoServiceRequest request);

	RestaurantDetailedInfoResponse getRestaurantDetailedInfo(RestaurantDetailedInfoServiceRequest request);

	List<RestaurantResponse> getRestaurants();
}
