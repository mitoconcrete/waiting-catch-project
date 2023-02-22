package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class DeleteBlacklistByRestaurantServiceRequest {
	private final Long blacklistId;
	private final Long sellerId;

	public DeleteBlacklistByRestaurantServiceRequest(Long blacklistId, Long sellerId) {
		this.blacklistId = blacklistId;
		this.sellerId = sellerId;
	}
}