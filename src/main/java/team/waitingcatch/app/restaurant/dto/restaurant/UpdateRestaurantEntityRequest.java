package team.waitingcatch.app.restaurant.dto.restaurant;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class UpdateRestaurantEntityRequest {

	private final String images;
	@NotNull
	private final String phoneNumber;
	@NotNull
	private final int capacity;
	@NotNull
	private final String description;
	@NotNull
	private final Long sellerId;
	@NotNull
	private final String openTime;
	@NotNull
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
