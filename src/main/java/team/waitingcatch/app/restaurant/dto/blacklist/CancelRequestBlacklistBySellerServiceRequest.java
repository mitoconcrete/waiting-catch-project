package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class CancelRequestBlacklistBySellerServiceRequest {
	private final Long requestBlackListId;
	private final Long sellerId;

	public CancelRequestBlacklistBySellerServiceRequest(Long requestBlackListId, Long sellerId) {
		this.requestBlackListId = requestBlackListId;
		this.sellerId = sellerId;
	}
}