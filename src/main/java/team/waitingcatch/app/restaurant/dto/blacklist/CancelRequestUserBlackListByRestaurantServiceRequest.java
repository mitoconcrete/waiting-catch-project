package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class CancelRequestUserBlackListByRestaurantServiceRequest {
	private final Long blacklistId;
	private final Long sellerId;

	public CancelRequestUserBlackListByRestaurantServiceRequest(Long blacklistId, Long sellerId) {
		this.blacklistId = blacklistId;
		this.sellerId = sellerId;
	}
}