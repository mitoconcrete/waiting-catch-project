package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class DeleteUserBlackListByRestaurantServiceRequest {
	private final Long blacklistId;
	private final Long sellerId;

	public DeleteUserBlackListByRestaurantServiceRequest(Long blacklistId, Long sellerId) {
		this.blacklistId = blacklistId;
		this.sellerId = sellerId;
	}
}
