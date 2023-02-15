package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;

@Getter
public class ApproveSignUpSellerResponse {
	private final String username;
	private final String password;

	public ApproveSignUpSellerResponse(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
