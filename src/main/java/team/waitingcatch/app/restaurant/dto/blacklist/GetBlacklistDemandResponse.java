package team.waitingcatch.app.restaurant.dto.blacklist;

import java.time.LocalDateTime;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.BlacklistDemand;

@Getter
public class GetBlacklistDemandResponse {
	private final Long id;
	private final String customerName;
	private final String sellerName;
	private final String description;
	private final String status;
	private final LocalDateTime createdDate;
	private final LocalDateTime modifiedDate;

	public GetBlacklistDemandResponse(BlacklistDemand blackListDemand) {
		this.id = blackListDemand.getId();
		this.customerName = blackListDemand.getUser().getUsername();
		this.sellerName = blackListDemand.getRestaurant().getUser().getUsername();
		this.description = blackListDemand.getDescription();
		this.status = blackListDemand.getStatus().getValue();
		this.createdDate = blackListDemand.getCreatedDate();
		this.modifiedDate = blackListDemand.getModifiedDate();
	}
}