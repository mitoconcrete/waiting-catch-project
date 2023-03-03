package team.waitingcatch.app.lineup.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

@Getter
public class TodayLineupResponse {
	private final long id;
	private final long userId;
	private final int waitingNumber;
	private final String name;
	private final int numOfMembers;
	private final ArrivalStatusEnum status;
	private final int callCount;
	private final LocalDateTime startedAt;
	private final LocalDateTime arrivedAt;

	@QueryProjection
	public TodayLineupResponse(long id, long userId, int waitingNumber, String name, int numOfMembers, ArrivalStatusEnum status,
		int callCount, LocalDateTime startedAt, LocalDateTime arrivedAt) {

		this.id = id;
		this.userId = userId;
		this.waitingNumber = waitingNumber;
		this.name = name;
		this.numOfMembers = numOfMembers;
		this.status = status;
		this.callCount = callCount;
		this.startedAt = startedAt;
		this.arrivedAt = arrivedAt;
	}
}