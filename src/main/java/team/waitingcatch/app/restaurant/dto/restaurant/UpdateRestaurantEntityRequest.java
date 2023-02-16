package team.waitingcatch.app.restaurant.dto.restaurant;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class UpdateRestaurantEntityRequest {

	@NotNull
	private final String images;
	@NotNull
	private final String phoneNumber;
	@NotNull
	private final int capacity;
	@NotNull
	private final String description;
	@NotNull
	private final String sellerName;
	@NotNull
	private final String openTime;
	@NotNull
	private final String closeTime;

	public UpdateRestaurantEntityRequest(UpdateRestaurantServiceRequest updateRestaurantServiceRequest, String url) {
		this.images = url;
		this.phoneNumber = updateRestaurantServiceRequest.getPhoneNumber();
		this.capacity = updateRestaurantServiceRequest.getCapacity();
		this.description = updateRestaurantServiceRequest.getDescription();
		this.sellerName = updateRestaurantServiceRequest.getSellerName();
		this.openTime = updateRestaurantServiceRequest.getOpenTime();
		this.closeTime = updateRestaurantServiceRequest.getCloseTime();
	}
}
