package team.waitingcatch.app.restaurant.dto.requestseller;

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
