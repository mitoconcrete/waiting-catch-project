package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class DeleteUserBlackListByRestaurantServiceRequest {
	@NotNull
	private final Long blacklistId;
	@NotNull
	private final Long sellerId;

	public DeleteUserBlackListByRestaurantServiceRequest(Long blacklistId, Long sellerId) {
		this.blacklistId = blacklistId;
		this.sellerId = sellerId;
	}
}
