package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Restaurant;

@Getter
public class RestaurantDetailedInfoResponse {
	private final String name;
	private final String province;
	private final String city;
	private final String street;
	private final String phoneNumber;
	private final String description;
	// private final String openTime;
	// private final String closeTime;

	public RestaurantDetailedInfoResponse(Restaurant restaurant) {
		this.name = restaurant.getName();
		this.province = restaurant.getProvince();
		this.city = restaurant.getCity();
		this.street = restaurant.getStreet();
		this.phoneNumber = restaurant.getPhoneNumber();
		this.description = restaurant.getDescription();

	}
}
