package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Restaurant;

@Getter
public class RestaurantResponse {
	private final String username;
	private final String email;
	private final String phoneNumber;
	private final String restaurantName;
	private Long category;
	private final double latitude;
	private final double longitude;
	private final String province;
	private final String city;
	private final String street;
	private final String searchKeywords;

	public RestaurantResponse(Restaurant restaurant) {
		this.username = restaurant.getUser().getUsername();
		this.email = restaurant.getUser().getEmail();
		this.phoneNumber = restaurant.getPhoneNumber();
		this.restaurantName = restaurant.getName();
		this.latitude = restaurant.getLatitude();
		this.longitude = restaurant.getLongitude();
		this.province = restaurant.getProvince();
		this.city = restaurant.getCity();
		this.street = restaurant.getStreet();
		this.searchKeywords = restaurant.getSearchKeywords();
	}
}
