package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class CancelBlacklistRequestServiceRequest {
	private final Long blacklistRequestId;
	private final Long sellerId;

	public CancelBlacklistRequestServiceRequest(Long blacklistRequestId, Long sellerId) {
		this.blacklistRequestId = blacklistRequestId;
		this.sellerId = sellerId;
	}
}