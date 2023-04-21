package team.waitingcatch.app.restaurant.dto.restaurant;

import java.util.List;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Restaurant;

@Getter
public class RestaurantBasicInfoResponse {
	private final String name;
	private final List<String> images;
	private final String zipCode;
	private final String address;
	private final String detailAddress;

	public RestaurantBasicInfoResponse(Restaurant restaurant) {
		this.name = restaurant.getName();
		this.images = restaurant.getImagePaths();
		this.zipCode = restaurant.getZipCode();
		this.address = restaurant.getAddress();
		this.detailAddress = restaurant.getDetailAddress();
	}
}