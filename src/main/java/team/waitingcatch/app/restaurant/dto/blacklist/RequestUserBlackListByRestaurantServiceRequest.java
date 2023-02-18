package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class RequestUserBlackListByRestaurantServiceRequest {
	@NotNull
	private final String description;
	@NotNull
	private final Long userId;
	@NotNull
	private final Long sellerId;

	public RequestUserBlackListByRestaurantServiceRequest(String description, Long userId, Long seller) {
		this.description = description;
		this.userId = userId;
		this.sellerId = seller;
	}
}
