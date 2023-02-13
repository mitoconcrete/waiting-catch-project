package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.restaurant.entity.SellerManagement;
import team.waitingcatch.app.user.entitiy.User;

@Getter
@NoArgsConstructor
public class ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest {
	//위도경도
	private User user;
	private String restaurantName;
	private String email;
	private String phoneNumber;
	private String categories;
	private String description;

	private Address address;
	private Position position;

	private String businessLicenseNo;

	private String searchKeyWords;

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
