package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.restaurant.entity.Restaurant;

@Getter
public class RestaurantBasicInfoResponse {
	private final String name;
	private final String images;
	private final Address address;
	// private final int rate;

	public RestaurantBasicInfoResponse(Restaurant restaurant) {
		this.name = restaurant.getName();
		this.images = restaurant.getImages();
		this.address = restaurant.getAddress();
	}
}
