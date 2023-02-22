package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.BlackList;

@Getter
public class GetBlackListResponse {
	private final Long blacklistId;
	@NotNull
	private final Long customerId;
	private final Long restaurantId;
	private final String customerName;
	private final String restaurantName;

	public GetBlackListResponse(BlackList blackList) {
		this.blacklistId = blackList.getId();
		this.customerId = blackList.getUser().getId();
		this.restaurantId = blackList.getRestaurant().getId();
		this.customerName = blackList.getUser().getUsername();
		this.restaurantName = blackList.getRestaurant().getName();
	}
}
