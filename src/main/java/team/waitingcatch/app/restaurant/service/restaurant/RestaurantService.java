package team.waitingcatch.app.restaurant.service.restaurant;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import team.waitingcatch.app.restaurant.dto.restaurant.DeleteRestaurantByAdminServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.GetRestaurantInfo;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithinRadiusResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithinRadiusServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantsResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantServiceRequest;
import team.waitingcatch.app.user.entitiy.User;

public interface RestaurantService {
	RestaurantBasicInfoResponse getRestaurantBasicInfo(RestaurantBasicInfoServiceRequest request);

	RestaurantDetailedInfoResponse getRestaurantDetailedInfo(RestaurantDetailedInfoServiceRequest request);

	Slice<SearchRestaurantsResponse> searchRestaurantsByKeyword(SearchRestaurantServiceRequest request);

	Slice<RestaurantsWithinRadiusResponse> getRestaurantsWithinRadius(
		RestaurantsWithinRadiusServiceRequest request);

	Page<RestaurantResponse> getRestaurants(Pageable pageable);

	boolean deleteRestaurantByAdmin(DeleteRestaurantByAdminServiceRequest deleteRestaurantByAdminServiceRequest);

	void updateRestaurant(UpdateRestaurantServiceRequest updateRestaurantServiceRequest) throws IOException;

	//	void createRestaurant(ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest request);

	Page<RestaurantResponse> getRestaurantsByRestaurantName(String searchVal, Pageable pageable);

	GetRestaurantInfo getRestaurantInfo(User user);

}