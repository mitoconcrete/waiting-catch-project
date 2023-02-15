package team.waitingcatch.app.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.restaurant.entity.SellerManagement;
import team.waitingcatch.app.user.enums.UserRoleEnum;

@Getter
@NoArgsConstructor
public class UserCreateServiceRequest {
	private String username;
	private String password;
	private String email;
	private String phoneNumber;
	private String name;
	private UserRoleEnum role;

	public UserCreateServiceRequest(SellerManagement sellerManagement, String passwordEncoded) {
		this.username = sellerManagement.getUsername();
		this.password = passwordEncoded;
		this.email = sellerManagement.getEmail();
		this.phoneNumber = sellerManagement.getPhoneNumber();
		this.role = UserRoleEnum.SELLER;
		this.name = sellerManagement.getName();
	}
}
