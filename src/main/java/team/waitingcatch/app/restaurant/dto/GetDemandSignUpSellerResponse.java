package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.restaurant.entity.SellerManagement;

@Getter
@NoArgsConstructor
public class GetDemandSignUpSellerResponse {
	private Long id;
	private String username;
	private String email;
	private String phoneNumber;
	private String restaurantName;
	private String categories;
	private float latitude;
	private float longitude;
	private String description;
	private String province;
	private String city;
	private String street;
	private String searchKeyWords;
	private String businessLicenseNo;
	private String name;

	public GetDemandSignUpSellerResponse(SellerManagement sellerManagement) {
		this.id = sellerManagement.getId();
		this.username = sellerManagement.getUsername();
		this.email = sellerManagement.getEmail();
		this.phoneNumber = sellerManagement.getPhoneNumber();
		this.restaurantName = sellerManagement.getRestaurantName();
		this.categories = sellerManagement.getCategories();
		this.latitude = sellerManagement.getPosition().getLatitude();
		this.longitude = sellerManagement.getPosition().getLongitude();
		this.description = sellerManagement.getDescription();
		this.province = sellerManagement.getAddress().getProvince();
		this.city = sellerManagement.getAddress().getCity();
		this.street = sellerManagement.getAddress().getStreet();
		this.searchKeyWords = sellerManagement.getSearchKeyWords();
		this.businessLicenseNo = sellerManagement.getBusinessLicenseNo();
		this.name = sellerManagement.getName();
	}
}
