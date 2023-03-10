package team.waitingcatch.app.restaurant.dto.requestseller;

import java.util.List;

import lombok.Getter;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.restaurant.entity.SellerManagement;
import team.waitingcatch.app.user.entitiy.User;

@Getter
public class ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest {
	private final User user;
	private final String restaurantName;
	private final String email;
	private final String phoneNumber;
	private final List<String> categories;
	private final String description;
	private final String zipCode;
	private final String address;
	private final String detailAddress;
	// private final Address address;
	private final Position position;
	private final String businessLicenseNo;
	private final List<String> searchKeyWords;

	public ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest(SellerManagement sellerManagement,
		User seller, List<String> searchKeywords) {
		this.user = seller;
		this.restaurantName = sellerManagement.getRestaurantName();
		this.email = sellerManagement.getEmail();
		this.phoneNumber = sellerManagement.getPhoneNumber();
		this.categories = sellerManagement.getCategories();
		this.description = sellerManagement.getDescription();
		this.zipCode = sellerManagement.getZipCode();
		this.address = sellerManagement.getAddress();
		this.detailAddress = sellerManagement.getDetailAddress();
		this.position = sellerManagement.getPosition();
		this.searchKeyWords = searchKeywords;
		this.businessLicenseNo = sellerManagement.getBusinessLicenseNo();
	}
}
