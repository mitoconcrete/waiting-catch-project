package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DemandSignUpSellerControllerRequest {

	private String username;
	private String name;
	private String email;
	private String phoneNumber;
	private String restaurantName;
	private String categories;
	private String description;
	private float latitude;
	private float longitude;
	private String province;
	private String city;
	private String street;

	private String businessLicenseNo;
	private String searchKeyWords;

}
