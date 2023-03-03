package team.waitingcatch.app.restaurant.dto.requestseller;

import java.util.List;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Restaurant;

@Getter
public class ConnectCategoryRestaurantServiceRequest {
	private final Restaurant restaurant;
	private final List<String> categoryIds;

	public ConnectCategoryRestaurantServiceRequest(Restaurant restaurant, List<String> categoryIds) {
		this.restaurant = restaurant;
		this.categoryIds = categoryIds;
	}
}
