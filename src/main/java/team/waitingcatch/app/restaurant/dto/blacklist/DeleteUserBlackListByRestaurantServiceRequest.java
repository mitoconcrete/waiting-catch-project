package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class DeleteUserBlackListByRestaurantServiceRequest {
	@NotNull
	private final Long userId;
	@NotNull
	private final String sellerName;

	public DeleteUserBlackListByRestaurantServiceRequest(Long userId, String sellerName) {
		this.userId = userId;
		this.sellerName = sellerName;
	}
}
