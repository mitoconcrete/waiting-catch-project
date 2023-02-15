package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.restaurant.DeleteRestaurantByAdminServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantResponse;
import team.waitingcatch.app.restaurant.service.restaurant.RestaurantService;

@RestController
@RequiredArgsConstructor
public class RestaurantController {
	private final RestaurantService restaurantService;

	// Customer
	@GetMapping("/restaurants/{restaurantId}")
	public RestaurantBasicInfoResponse getRestaurantBasicInfo(@PathVariable Long restaurantId) {
		RestaurantBasicInfoServiceRequest request = new RestaurantBasicInfoServiceRequest(restaurantId);
		return restaurantService.getRestaurantBasicInfo(request);
	}

	@GetMapping("/restaurants/{restaurantId}/details")
	public RestaurantDetailedInfoResponse getRestaurantDetailedInfo(@PathVariable Long restaurantId) {
		RestaurantDetailedInfoServiceRequest request = new RestaurantDetailedInfoServiceRequest(restaurantId);
		return restaurantService.getRestaurantDetailedInfo(request);
	}

	// Seller

	// Admin
	@GetMapping("/admin/restaurants")
	public List<RestaurantResponse> getRestaurants() {
		return restaurantService.getRestaurants();
	}

	//관리자가 레스토랑을 삭제 한다
	@DeleteMapping("/admin/restaurants/{restaurant_id}")
	public void deleteRestaurantByAdmin(@PathVariable Long restaurant_id) {
		DeleteRestaurantByAdminServiceRequest deleteRestaurantByAdminServiceRequest
			= new DeleteRestaurantByAdminServiceRequest(restaurant_id);
		restaurantService.deleteRestaurantByAdmin(deleteRestaurantByAdminServiceRequest);
	}

}
