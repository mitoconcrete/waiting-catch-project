package team.waitingcatch.app.restaurant.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.restaurant.entity.SellerManagement;
import team.waitingcatch.app.user.entitiy.User;

@Getter
public class ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest {
	@NotNull
	private final User user;
	@NotNull
	private final String restaurantName;
	@NotNull
	private final String email;
	@NotNull
	private final String phoneNumber;
	@NotNull
	private final String categories;
	@NotNull
	private final String description;
	@NotNull
	private final Address address;
	@NotNull
	private final Position position;
	@NotNull
	private final String businessLicenseNo;
	@NotNull
	private final String searchKeyWords;

	public ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest(SellerManagement sellerManagement,
		User seller) {
		this.user = seller;
		this.restaurantName = sellerManagement.getRestaurantName();
		this.email = sellerManagement.getEmail();
		this.phoneNumber = sellerManagement.getPhoneNumber();
		this.categories = sellerManagement.getCategories();
		this.description = sellerManagement.getDescription();
		this.address = sellerManagement.getAddress();
		this.position = sellerManagement.getPosition();
		this.searchKeyWords = sellerManagement.getSearchKeyWords();
		this.businessLicenseNo = sellerManagement.getBusinessLicenseNo();
	}
}
