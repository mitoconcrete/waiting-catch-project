package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class GetBlackListByRestaurantIdServiceRequest {
	private final Long sellerId;

	public GetBlackListByRestaurantIdServiceRequest(Long restaurantId) {
		this.sellerId = restaurantId;
	}
}
