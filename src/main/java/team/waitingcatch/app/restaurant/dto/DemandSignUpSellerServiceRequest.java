package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;

@Getter
@NoArgsConstructor
public class DemandSignUpSellerServiceRequest {
	private String username;
	private String email;
	private String phoneNumber;
	private String restaurantName;
	private String categories;
	private String description;

	private Position position;
	private Address address;

	private String searchKeyWords;

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
	}
}
