package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class GetBlackListByRestaurantServiceRequest {
	private final Long sellerId;

	public GetBlackListByRestaurantServiceRequest(Long sellerId) {
		this.sellerId = sellerId;
	}
}
