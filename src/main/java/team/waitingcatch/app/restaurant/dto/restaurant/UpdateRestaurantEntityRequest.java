package team.waitingcatch.app.restaurant.dto.restaurant;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class UpdateRestaurantEntityRequest {

	private final String phoneNumber;
	private final int capacity;
	private final String description;
	private final Long sellerId;
	private final String openTime;
	private final String closeTime;
	private final List<String> imagePaths = new ArrayList<>();

	public UpdateRestaurantEntityRequest(UpdateRestaurantServiceRequest updateRestaurantServiceRequest,
		List<String> imagePaths) {
		this.imagePaths.addAll(imagePaths);
		this.phoneNumber = updateRestaurantServiceRequest.getPhoneNumber();
		this.capacity = updateRestaurantServiceRequest.getCapacity();
		this.description = updateRestaurantServiceRequest.getDescription();
		this.sellerId = updateRestaurantServiceRequest.getSellerId();
		this.openTime = updateRestaurantServiceRequest.getOpenTime();
		this.closeTime = updateRestaurantServiceRequest.getCloseTime();
	}
}
