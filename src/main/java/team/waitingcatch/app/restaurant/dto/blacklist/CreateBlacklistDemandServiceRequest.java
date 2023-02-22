package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class CreateBlacklistDemandServiceRequest {
	private final Long sellerId;
	private final Long userId;
	private final String description;

	public CreateBlacklistDemandServiceRequest(Long seller, Long userId, String description) {
		this.sellerId = seller;
		this.userId = userId;
		this.description = description;
	}
}