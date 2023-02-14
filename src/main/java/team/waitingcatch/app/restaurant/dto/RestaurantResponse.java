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
		// this.category = restaurant.get;
		this.latitude = restaurant.getPosition().getLatitude();
		this.longitude = restaurant.getPosition().getLongitude();
		this.province = restaurant.getAddress().getProvince();
		this.city = restaurant.getAddress().getCity();
		this.street = restaurant.getAddress().getStreet();
		this.searchKeywords = restaurant.getSearchKeywords();
	}
}
