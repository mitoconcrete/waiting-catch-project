package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApproveSignUpSellerResponse {
	private String username;
	private String password;

	public ApproveSignUpSellerResponse(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
