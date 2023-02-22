package team.waitingcatch.app.restaurant.dto.restaurant;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.RestaurantInfo;

@Getter
public class RestaurantDetailedInfoResponse {
	private final String name;
	private final String province;
	private final String city;
	private final String street;
	private final String phoneNumber;
	private final String description;
	private final String openTime;
	private final String closeTime;

	public RestaurantDetailedInfoResponse(Restaurant restaurant, RestaurantInfo restaurantInfo) {
		this.name = restaurant.getName();
		this.province = restaurant.getProvince();
		this.city = restaurant.getCity();
		this.street = restaurant.getStreet();
		this.phoneNumber = restaurant.getPhoneNumber();
		this.description = restaurant.getDescription();
		this.openTime = restaurantInfo.getOpenTime();
		this.closeTime = restaurantInfo.getCloseTime();
	}
}
