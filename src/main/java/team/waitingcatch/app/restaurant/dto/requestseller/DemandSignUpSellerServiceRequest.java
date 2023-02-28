package team.waitingcatch.app.restaurant.dto.requestseller;

import lombok.Getter;
import team.waitingcatch.app.common.Position;

@Getter
public class DemandSignUpSellerServiceRequest {
	private final String username;
	private final String name;
	private final String email;
	private final String phoneNumber;
	private final String restaurantName;
	private final String categories;
	private final String description;
	private final Position position;
	private final String zipCode;
	private final String address;
	private final String detailAddress;
	// private final Address address;
	// private final String searchKeyWords;
	private final String businessLicenseNo;

	public DemandSignUpSellerServiceRequest(DemandSignUpSellerControllerRequest demandSignUpSellerControllerRequest,
		Position position) {
		this.username = demandSignUpSellerControllerRequest.getUsername();
		this.email = demandSignUpSellerControllerRequest.getEmail();
		this.phoneNumber = demandSignUpSellerControllerRequest.getPhoneNumber();
		this.restaurantName = demandSignUpSellerControllerRequest.getRestaurantName();
		this.categories = demandSignUpSellerControllerRequest.getCategories();
		this.description = demandSignUpSellerControllerRequest.getDescription();
		this.position = position;
		this.zipCode = demandSignUpSellerControllerRequest.getZipCode();
		this.address = demandSignUpSellerControllerRequest.getAddress();
		this.detailAddress = demandSignUpSellerControllerRequest.getDetailAddress();
		// this.address = address;
		// this.searchKeyWords = demandSignUpSellerControllerRequest.getSearchKeyWords();
		this.businessLicenseNo = demandSignUpSellerControllerRequest.getBusinessLicenseNo();
		this.name = demandSignUpSellerControllerRequest.getName();
	}
}
