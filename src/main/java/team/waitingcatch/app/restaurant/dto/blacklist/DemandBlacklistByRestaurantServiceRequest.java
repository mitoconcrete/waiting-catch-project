package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class DemandBlacklistByRestaurantServiceRequest {
	private final Long sellerId;
	private final Long userId;
	private final String description;

	public DemandBlacklistByRestaurantServiceRequest(Long sellerId, Long userId,String description) {
		this.sellerId = sellerId;
		this.userId = userId;
		this.description = description;
	}
}