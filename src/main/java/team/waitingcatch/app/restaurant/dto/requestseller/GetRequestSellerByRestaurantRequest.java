package team.waitingcatch.app.restaurant.dto.requestseller;

import lombok.Getter;

@Getter
public class GetRequestSellerByRestaurantRequest {
	private final String requestSellerName;
	private final String email;

	public GetRequestSellerByRestaurantRequest(String requestSellerName, String email) {
		this.requestSellerName = requestSellerName;
		this.email = email;
	}
}

