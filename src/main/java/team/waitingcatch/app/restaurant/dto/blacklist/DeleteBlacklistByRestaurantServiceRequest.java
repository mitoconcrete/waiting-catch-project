package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
<<<<<<< Updated upstream:src/main/java/team/waitingcatch/app/restaurant/dto/blacklist/CancelRequestUserBlackListByRestaurantServiceRequest.java
public class CancelRequestUserBlackListByRestaurantServiceRequest {
	private final Long blacklistId;
	private final Long sellerId;

	public CancelRequestUserBlackListByRestaurantServiceRequest(Long blacklistId, Long sellerId) {
=======
public class DeleteBlacklistByRestaurantServiceRequest {
	@NotNull
	private final Long blacklistId;

	@NotNull
	private final Long sellerId;

	public DeleteBlacklistByRestaurantServiceRequest(Long blacklistId, Long sellerId) {
>>>>>>> Stashed changes:src/main/java/team/waitingcatch/app/restaurant/dto/blacklist/DeleteBlacklistByRestaurantServiceRequest.java
		this.blacklistId = blacklistId;
		this.sellerId = sellerId;
	}
}