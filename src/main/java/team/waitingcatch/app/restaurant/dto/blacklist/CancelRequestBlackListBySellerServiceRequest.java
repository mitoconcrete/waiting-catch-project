package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class CancelRequestBlackListBySellerServiceRequest {
	private final Long requestBlackListId;
	private final Long sellerId;

	public CancelRequestBlackListBySellerServiceRequest(Long requestBlackListId, Long sellerId) {
		this.requestBlackListId = requestBlackListId;
		this.sellerId = sellerId;
	}
}
