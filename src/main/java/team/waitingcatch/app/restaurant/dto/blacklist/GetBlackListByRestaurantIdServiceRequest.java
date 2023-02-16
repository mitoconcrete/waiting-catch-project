package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class GetBlackListByRestaurantIdServiceRequest {
	private final Long restaurantId;

	public GetBlackListByRestaurantIdServiceRequest(Long restaurantId) {
		this.restaurantId = restaurantId;
	}
}
