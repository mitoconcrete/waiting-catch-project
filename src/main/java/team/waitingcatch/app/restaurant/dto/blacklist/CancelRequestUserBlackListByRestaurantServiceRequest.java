package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class CancelRequestUserBlackListByRestaurantServiceRequest {
	@NotNull
	private final Long requestBlackListId;
	@NotNull
	private final Long sellerId;

	public CancelRequestUserBlackListByRestaurantServiceRequest(Long requestBlackListId, Long sellerId) {
		this.requestBlackListId = requestBlackListId;
		this.sellerId = sellerId;
	}
}
