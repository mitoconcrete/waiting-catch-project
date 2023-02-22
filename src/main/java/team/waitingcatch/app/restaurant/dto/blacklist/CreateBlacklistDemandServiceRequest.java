package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class BlacklistDemandServiceRequest {
	private final Long sellerId;
	private final Long userId;
	private final String description;

	public BlacklistDemandServiceRequest(Long seller, Long userId, String description) {
		this.sellerId = seller;
		this.userId = userId;
		this.description = description;
	}
}