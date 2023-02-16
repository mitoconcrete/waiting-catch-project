package team.waitingcatch.app.restaurant.dto.requestseller;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.SellerManagement;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;

@Getter
public class GetDemandSignUpSellerResponse {
	private final Long id;
	private final String username;
	private final String email;
	private final String phoneNumber;
	private final String restaurantName;
	private final String categories;
	private final double latitude;
	private final double longitude;
	private final String description;
	private final String province;
	private final String city;
	private final String street;
	private final String searchKeyWords;
	private final String businessLicenseNo;
	private final String name;
	private final AcceptedStatusEnum status;

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
		this.status = sellerManagement.getStatus();
	}
}
