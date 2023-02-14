package team.waitingcatch.app.restaurant.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;

@Getter
public class DemandSignUpSellerServiceRequest {
	@NotNull
	private final String username;
	@NotNull
	private final String name;
	@NotNull
	private final String email;
	@NotNull
	private final String phoneNumber;
	@NotNull
	private final String restaurantName;
	@NotNull
	private final String categories;
	@NotNull
	private final String description;
	@NotNull
	private final Position position;
	@NotNull
	private final Address address;
	@NotNull
	private final String searchKeyWords;
	@NotNull
	private final String businessLicenseNo;

	public DemandSignUpSellerServiceRequest(DemandSignUpSellerControllerRequest demandSignUpSellerControllerRequest,
		Address address, Position position) {
		this.username = demandSignUpSellerControllerRequest.getUsername();
		this.email = demandSignUpSellerControllerRequest.getEmail();
		this.phoneNumber = demandSignUpSellerControllerRequest.getPhoneNumber();
		this.restaurantName = demandSignUpSellerControllerRequest.getRestaurantName();
		this.categories = demandSignUpSellerControllerRequest.getCategories();
		this.description = demandSignUpSellerControllerRequest.getDescription();
		this.position = position;
		this.address = address;
		this.searchKeyWords = demandSignUpSellerControllerRequest.getSearchKeyWords();
		this.businessLicenseNo = demandSignUpSellerControllerRequest.getBusinessLicenseNo();
		this.name = demandSignUpSellerControllerRequest.getName();
	}
}
