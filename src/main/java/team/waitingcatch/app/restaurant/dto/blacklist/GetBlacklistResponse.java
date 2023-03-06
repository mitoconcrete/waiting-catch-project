package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Blacklist;

@Getter
public class GetBlacklistResponse {
	private final Long blacklistId;
	private final Long customerId;
	private final Long restaurantId;
	private final String customerName;
	private final String restaurantName;

	public GetBlacklistResponse(Blacklist blacklist) {
		this.blacklistId = blacklist.getId();
		this.customerId = blacklist.getUser().getId();
		this.restaurantId = blacklist.getRestaurant().getId();
		this.customerName = blacklist.getUser().getUsername();
		this.restaurantName = blacklist.getRestaurant().getName();
	}
}