package team.waitingcatch.app.restaurant.dto.restaurant;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public class UpdateRestaurantServiceRequest {

	private final List<MultipartFile> files;
	private final String phoneNumber;
	private final int capacity;
	private final String description;
	private final Long sellerId;
	private final String openTime;
	private final String closeTime;

	public UpdateRestaurantServiceRequest(UpdateRestaurantControllerRequest updateRestaurantControllerRequest,
		List<MultipartFile> multipartFile, Long userId) {
		this.files = multipartFile;
		this.phoneNumber = updateRestaurantControllerRequest.getPhoneNumber();
		this.capacity = updateRestaurantControllerRequest.getCapacity();
		this.description = updateRestaurantControllerRequest.getDescription();
		this.sellerId = userId;
		this.openTime = updateRestaurantControllerRequest.getOpenTime();
		this.closeTime = updateRestaurantControllerRequest.getCloseTime();
	}

}
