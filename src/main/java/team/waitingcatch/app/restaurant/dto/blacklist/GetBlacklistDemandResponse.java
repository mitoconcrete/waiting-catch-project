package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.BlacklistRequest;

@Getter
public class GetRequestBlacklistResponse {
	@NotNull
	private final Long blacklistRequestId;

	@NotNull
	private final String customerName;

	@NotNull
	private final String sellerName;

	@NotNull
	private final String description;

	public GetRequestBlacklistResponse(BlacklistRequest blackListRequest) {
		this.blacklistRequestId = blackListRequest.getId();
		this.customerName = blackListRequest.getUser().getUsername();
		this.sellerName = blackListRequest.getRestaurant().getUser().getUsername();
		this.description = blackListRequest.getDescription();
	}
}