package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class CancelBlacklistByRestaurantServiceRequest {
	private final Long blacklistId;
	private final Long sellerId;

	public CancelBlacklistByRestaurantServiceRequest(Long blacklistId, Long sellerId) {
		this.blacklistId = blacklistId;
		this.sellerId = sellerId;
	}
}