package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class CancelRequestUserBlackListByRestaurantServiceRequest {
	@NotNull
	private final Long userId;
	@NotNull
	private final String sellerName;

	public CancelRequestUserBlackListByRestaurantServiceRequest(Long userId, String sellerName) {
		this.userId = userId;
		this.sellerName = sellerName;
	}
}
