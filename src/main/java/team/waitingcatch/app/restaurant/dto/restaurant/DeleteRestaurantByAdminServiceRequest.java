package team.waitingcatch.app.restaurant.dto.restaurant;

import lombok.Getter;

@Getter
public class DeleteRestaurantByAdminServiceRequest {
	private final Long restaurantId;

	public DeleteRestaurantByAdminServiceRequest(Long restaurantId) {
		this.restaurantId = restaurantId;
	}
}
