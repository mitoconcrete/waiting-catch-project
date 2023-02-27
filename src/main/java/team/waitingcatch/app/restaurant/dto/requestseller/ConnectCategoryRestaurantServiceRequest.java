package team.waitingcatch.app.restaurant.dto.requestseller;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Restaurant;

@Getter
public class ConnectCategoryRestaurantServiceRequest {
	private final Restaurant restaurant;
	private final String categoryIds;

	public ConnectCategoryRestaurantServiceRequest(Restaurant restaurant, String categoryIds) {
		this.restaurant = restaurant;
		this.categoryIds = categoryIds;
	}
}
