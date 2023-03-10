package team.waitingcatch.app.restaurant.dto.restaurant;

import java.util.List;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Restaurant;

@Getter
public class RestaurantBasicInfoResponse {
	private final String name;
	private final List<String> images;
	// private final String province;
	// private final String city;
	// private final String street;
	private final String zipCode;
	private final String address;
	private final String detailAddress;
	private int rate;

	public RestaurantBasicInfoResponse(Restaurant restaurant) {
		this.name = restaurant.getName();
		//this.images = restaurant.getImages();
		this.images = restaurant.getImagePaths();
		// this.province = restaurant.getProvince();
		// this.city = restaurant.getCity();
		// this.street = restaurant.getStreet();
		this.zipCode = restaurant.getZipCode();
		this.address = restaurant.getAddress();
		this.detailAddress = restaurant.getDetailAddress();
	}
}