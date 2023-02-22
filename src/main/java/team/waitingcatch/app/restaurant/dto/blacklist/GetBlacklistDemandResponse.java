package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.BlacklistDemand;

@Getter
public class GetBlacklistDemandResponse {
	private final Long blacklistRequestId;
	private final String customerName;
	private final String sellerName;
	private final String description;

	public GetBlacklistDemandResponse(BlacklistDemand blackListDemand) {
		this.blacklistRequestId = blackListDemand.getId();
		this.customerName = blackListDemand.getUser().getUsername();
		this.sellerName = blackListDemand.getRestaurant().getUser().getUsername();
		this.description = blackListDemand.getDescription();
	}
}