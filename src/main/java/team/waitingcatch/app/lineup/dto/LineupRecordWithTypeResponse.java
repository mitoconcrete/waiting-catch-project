package team.waitingcatch.app.lineup.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;
import team.waitingcatch.app.lineup.enums.StoredLineupTableNameEnum;

@Getter
public class LineupRecordWithTypeResponse {
	private final StoredLineupTableNameEnum type;
	private final long lineupId;
	private final long restaurantId;
	private final String restaurantName;
	private final int numOfMembers;
	private final ArrivalStatusEnum status;
	private final boolean isReviewed;
	private final LocalDateTime startedAt;
	private final LocalDateTime arrivedAt;

	@QueryProjection
	public LineupRecordWithTypeResponse(StoredLineupTableNameEnum type, long lineupId, long restaurantId,
		String restaurantName, int numOfMembers, ArrivalStatusEnum status, boolean isReviewed, LocalDateTime startedAt,
		LocalDateTime arrivedAt) {

		this.type = type;
		this.lineupId = lineupId;
		this.restaurantId = restaurantId;
		this.restaurantName = restaurantName;
		this.numOfMembers = numOfMembers;
		this.status = status;
		this.isReviewed = isReviewed;
		this.startedAt = startedAt;
		this.arrivedAt = arrivedAt;
	}

	public static LineupRecordWithTypeResponse of(LineupRecordResponse recordResponse, StoredLineupTableNameEnum type) {
		return new LineupRecordWithTypeResponse(type, recordResponse.getLineupId(), recordResponse.getRestaurantId(),
			recordResponse.getRestaurantName(), recordResponse.getNumOfMembers(), recordResponse.getStatus(),
			recordResponse.isReviewed(), recordResponse.getStartedAt(), recordResponse.getArrivedAt());
	}
}