package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class CancelBlacklistDemandServiceRequest {
	private final Long blacklistDemandId;
	private final Long sellerId;

	public CancelBlacklistDemandServiceRequest(Long blacklistDemandId, Long sellerId) {
		this.blacklistDemandId = blacklistDemandId;
		this.sellerId = sellerId;
	}
}