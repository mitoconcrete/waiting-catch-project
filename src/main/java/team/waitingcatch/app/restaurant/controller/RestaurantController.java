package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.RestaurantResponse;
import team.waitingcatch.app.restaurant.service.restaurant.RestaurantService;

@RestController
@RequiredArgsConstructor
public class RestaurantController {
	private final RestaurantService restaurantService;

	@GetMapping("/admin/restaurants")
	public List<RestaurantResponse> getRestaurants() {
		return restaurantService.getRestaurants();
	}
}
