package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class GetBlacklistByRestaurantIdServiceRequest {
	private final Long restaurantId;

	public GetBlacklistByRestaurantIdServiceRequest(Long restaurantId) {
		this.restaurantId = restaurantId;
	}
}
