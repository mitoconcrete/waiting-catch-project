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
	private final String sellerName;

	public RequestUserBlackListByRestaurantServiceRequest(String description, Long userId, String username) {
		this.description = description;
		this.userId = userId;
		this.sellerName = username;
	}
}
