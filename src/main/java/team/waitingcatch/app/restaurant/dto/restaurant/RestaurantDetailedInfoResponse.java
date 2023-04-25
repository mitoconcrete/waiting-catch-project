package team.waitingcatch.app.restaurant.dto.restaurant;

import java.util.List;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.RestaurantInfo;

@Getter
public class RestaurantDetailedInfoResponse {
	private final Long id;
	private final String name;
	private final String address;
	private final String phoneNumber;
	private final String description;
	private final String openTime;
	private final String closeTime;
	private final List<String> category;
	private final float rate;

	public RestaurantDetailedInfoResponse(Restaurant restaurant, RestaurantInfo restaurantInfo) {
		this.id = restaurant.getId();
		this.category = restaurant.getSearchKeywords();
		this.name = restaurant.getName();
		this.address = restaurant.getAddress();
		this.phoneNumber = restaurant.getPhoneNumber();
		this.description = restaurant.getDescription();
		this.openTime = restaurantInfo.getOpenTime();
		this.closeTime = restaurantInfo.getCloseTime();
		this.rate = restaurantInfo.getRate();
	}
}