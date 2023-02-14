package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.RestaurantBasicInfoResponse;
import team.waitingcatch.app.restaurant.dto.RestaurantBasicInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.RestaurantDetailedInfoResponse;
import team.waitingcatch.app.restaurant.dto.RestaurantDetailedInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.RestaurantResponse;
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
}
