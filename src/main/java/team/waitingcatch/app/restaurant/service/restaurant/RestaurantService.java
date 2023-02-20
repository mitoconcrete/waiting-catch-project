package team.waitingcatch.app.restaurant.service.restaurant;

import java.io.IOException;
import java.util.List;

import team.waitingcatch.app.restaurant.dto.restaurant.DeleteRestaurantByAdminServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantServiceRequest;

public interface RestaurantService {
	RestaurantBasicInfoResponse getRestaurantBasicInfo(RestaurantBasicInfoServiceRequest request);

	RestaurantDetailedInfoResponse getRestaurantDetailedInfo(RestaurantDetailedInfoServiceRequest request);

	List<RestaurantResponse> getRestaurants();

	void deleteRestaurantByAdmin(DeleteRestaurantByAdminServiceRequest deleteRestaurantByAdminServiceRequest);

	void updateRestaurant(UpdateRestaurantServiceRequest updateRestaurantServiceRequest) throws IOException;
}