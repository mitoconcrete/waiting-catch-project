package team.waitingcatch.app.restaurant.dto.restaurant;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public class UpdateRestaurantServiceRequest {

	@NotNull
	private final List<MultipartFile> files;
	@NotNull
	private final String phoneNumber;
	@NotNull
	private final int capacity;
	@NotNull
	private final String description;
	@NotNull
	private final Long sellerId;
	@NotNull
	private String openTime;
	@NotNull
	private String closeTime;

	public UpdateRestaurantServiceRequest(UpdateRestaurantControllerRequest updateRestaurantControllerRequest,
		List<MultipartFile> multipartFile, Long sellerId) {
		this.files = multipartFile;
		this.phoneNumber = updateRestaurantControllerRequest.getPhoneNumber();
		this.capacity = updateRestaurantControllerRequest.getCapacity();
		this.description = updateRestaurantControllerRequest.getDescription();
		this.sellerId = sellerId;
		this.openTime = updateRestaurantControllerRequest.getOpenTime();
		this.closeTime = updateRestaurantControllerRequest.getCloseTime();
	}

}
