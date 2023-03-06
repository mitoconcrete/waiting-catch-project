package team.waitingcatch.app.restaurant.dto.restaurant;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public class UpdateRestaurantServiceRequest {
	private final Long sellerId;
	private final String phoneNumber;
	private final int capacity;
	private final String description;
	private final String openTime;
	private final String closeTime;
	private final List<MultipartFile> images = new ArrayList<>();

	public UpdateRestaurantServiceRequest(UpdateRestaurantControllerRequest updateRestaurantControllerRequest,
		List<MultipartFile> images, Long userId) {

		this.sellerId = userId;
		this.phoneNumber = updateRestaurantControllerRequest.getPhoneNumber();
		this.capacity = updateRestaurantControllerRequest.getCapacity();
		this.description = updateRestaurantControllerRequest.getDescription();
		this.openTime = updateRestaurantControllerRequest.getOpenTime();
		this.closeTime = updateRestaurantControllerRequest.getCloseTime();
		if (images != null) {
			this.images.addAll(images);
		}
	}
}