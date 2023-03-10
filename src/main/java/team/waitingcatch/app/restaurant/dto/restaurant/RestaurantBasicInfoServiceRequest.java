package team.waitingcatch.app.restaurant.dto.restaurant;

import lombok.Getter;

@Getter
public class RestaurantBasicInfoServiceRequest {
	private final Long restaurantId;

	public RestaurantBasicInfoServiceRequest(Long restaurantId) {
		this.restaurantId = restaurantId;
	}
}
