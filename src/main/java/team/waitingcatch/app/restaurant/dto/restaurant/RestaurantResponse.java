package team.waitingcatch.app.restaurant.dto.restaurant;

import java.util.List;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Restaurant;

@Getter
public class RestaurantResponse {
	private final Long id;
	private final String name;
	private final String email;
	private final String phoneNumber;
	private final String restaurantName;
	private final double latitude;
	private final double longitude;
	private final String province;
	private final String city;
	private final String street;
	private final List<String> searchKeywords;
	private final String category;

	private final List<String> images;
	private final String description;
	private final int capacity;
	private final String businessNo;

	private final boolean isDeleted;

	public RestaurantResponse(Restaurant restaurant) {
		this.id = restaurant.getId();
		this.name = restaurant.getUser().getUsername();
		this.email = restaurant.getUser().getEmail();
		this.phoneNumber = restaurant.getPhoneNumber();
		this.restaurantName = restaurant.getName();
		this.latitude = restaurant.getLatitude();
		this.longitude = restaurant.getLongitude();
		this.province = restaurant.getProvince();
		this.city = restaurant.getCity();
		this.street = restaurant.getStreet();
		this.searchKeywords = restaurant.getSearchKeywords();
		this.category = restaurant.getCategory();
		this.images = restaurant.getImagePaths();
		this.description = restaurant.getDescription();
		this.capacity = restaurant.getCapacity();
		this.businessNo = restaurant.getBusinessLicenseNo();
		this.isDeleted = restaurant.isDeleted();
	}
}
