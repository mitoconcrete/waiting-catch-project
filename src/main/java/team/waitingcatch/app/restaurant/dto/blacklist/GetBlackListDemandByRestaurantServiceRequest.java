package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class GetBlackListDemandByRestaurantServiceRequest {
	private final Long sellerId;

	public GetBlackListDemandByRestaurantServiceRequest(Long sellerId) {
		this.sellerId = sellerId;
	}
}
