package team.waitingcatch.app.restaurant.dto.restaurant;

import lombok.Getter;

@Getter
public class UpdateRestaurantEntityRequest {

	private final String images;
	private final String phoneNumber;
	private final int capacity;
	private final String description;
	private final Long sellerId;
	private final String openTime;
	private final String closeTime;

	public UpdateRestaurantEntityRequest(UpdateRestaurantServiceRequest updateRestaurantServiceRequest,
		String url) {
		this.images = url;
		this.phoneNumber = updateRestaurantServiceRequest.getPhoneNumber();
		this.capacity = updateRestaurantServiceRequest.getCapacity();
		this.description = updateRestaurantServiceRequest.getDescription();
		this.sellerId = updateRestaurantServiceRequest.getSellerId();
		this.openTime = updateRestaurantServiceRequest.getOpenTime();
		this.closeTime = updateRestaurantServiceRequest.getCloseTime();
	}
}
