package team.waitingcatch.app.restaurant.dto.restaurant;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Restaurant;

@Getter
public class RestaurantBasicInfoResponse {
	private final String name;
	private final String images;
	private final String province;
	private final String city;
	private final String street;
	private int rate;

	public RestaurantBasicInfoResponse(Restaurant restaurant) {
		this.name = restaurant.getName();
		//this.images = restaurant.getImages();
		this.images = restaurant.getImages();
		this.province = restaurant.getProvince();
		this.city = restaurant.getCity();
		this.street = restaurant.getStreet();
	}
}