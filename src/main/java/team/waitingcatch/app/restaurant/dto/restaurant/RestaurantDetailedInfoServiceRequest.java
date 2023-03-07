package team.waitingcatch.app.restaurant.dto.restaurant;

import lombok.Getter;

@Getter
public class RestaurantDetailedInfoServiceRequest {
	private final Long restaurantId;

	public RestaurantDetailedInfoServiceRequest(Long restaurantId) {
		this.restaurantId = restaurantId;
	}
}
