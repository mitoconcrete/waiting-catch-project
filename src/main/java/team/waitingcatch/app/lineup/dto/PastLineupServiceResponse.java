package team.waitingcatch.app.lineup.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

@Getter
public class PastLineupServiceResponse {
	private final long restaurantId;
	private final String restaurantName;
	private final int numOfMembers;
	private final ArrivalStatusEnum status;
	private final boolean isReviewed;
	private final LocalDateTime startedAt;
	private final LocalDateTime endedAt;

	@QueryProjection
	public PastLineupServiceResponse(long restaurantId, String restaurantName, int numOfMembers,
		ArrivalStatusEnum status,
		boolean isReviewed, LocalDateTime startedAt, LocalDateTime endedAt) {
		this.restaurantId = restaurantId;
		this.restaurantName = restaurantName;
		this.numOfMembers = numOfMembers;
		this.status = status;
		this.isReviewed = isReviewed;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
	}
}