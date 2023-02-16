package team.waitingcatch.app.restaurant.dto.restaurant;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public class UpdateRestaurantServiceRequest {

	@NotNull
	private final MultipartFile file;
	@NotNull
	private final String phoneNumber;
	@NotNull
	private final int capacity;
	@NotNull
	private final String description;
	@NotNull
	private final String sellerName;
	@NotNull
	private String openTime;
	@NotNull
	private String closeTime;

	public UpdateRestaurantServiceRequest(UpdateRestaurantControllerRequest updateRestaurantControllerRequest,
		MultipartFile multipartFile, String username) {
		this.file = multipartFile;
		this.phoneNumber = updateRestaurantControllerRequest.getPhoneNumber();
		this.capacity = updateRestaurantControllerRequest.getCapacity();
		this.description = updateRestaurantControllerRequest.getDescription();
		this.sellerName = username;
		this.openTime = updateRestaurantControllerRequest.getOpenTime();
		this.closeTime = updateRestaurantControllerRequest.getCloseTime();
	}

}
