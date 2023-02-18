package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class GetBlackListByRestaurantIdServiceRequest {
	private final Long userId;

	public GetBlackListByRestaurantIdServiceRequest(Long restaurantId) {
		this.userId = restaurantId;
	}
}
