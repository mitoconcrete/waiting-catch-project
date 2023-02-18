package team.waitingcatch.app.restaurant.service.restaurant;

import java.io.IOException;
import java.util.List;

import team.waitingcatch.app.restaurant.dto.restaurant.DeleteRestaurantByAdminServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithin3kmRadiusResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithin3kmRadiusServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantsResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantServiceRequest;

public interface RestaurantService {
	RestaurantBasicInfoResponse getRestaurantBasicInfo(RestaurantBasicInfoServiceRequest request);

	RestaurantDetailedInfoResponse getRestaurantDetailedInfo(RestaurantDetailedInfoServiceRequest request);

	List<SearchRestaurantsResponse> searchRestaurantsByKeyword(SearchRestaurantServiceRequest request);

	List<RestaurantsWithin3kmRadiusResponse> getRestaurantsWithin3kmRadius(
		RestaurantsWithin3kmRadiusServiceRequest request);

	List<RestaurantResponse> getRestaurants();

	void deleteRestaurantByAdmin(DeleteRestaurantByAdminServiceRequest deleteRestaurantByAdminServiceRequest);

	void updateRestaurant(UpdateRestaurantServiceRequest updateRestaurantServiceRequest) throws IOException;
}
