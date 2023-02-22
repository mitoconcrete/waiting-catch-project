package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class RequestUserBlackListByRestaurantServiceRequest {
	private final String description;
	private final Long userId;
	private final Long sellerId;

	public RequestUserBlackListByRestaurantServiceRequest(String description, Long userId, Long seller) {
		this.description = description;
		this.userId = userId;
		this.sellerId = seller;
	}
}
