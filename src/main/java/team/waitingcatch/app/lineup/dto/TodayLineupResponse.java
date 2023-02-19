package team.waitingcatch.app.lineup.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

@Getter
public class TodayLineupResponse {
	private final int waitingNumber;
	private final int numOfMember;
	private final ArrivalStatusEnum status;
	private final int callCount;
	private final LocalDateTime startedAt;
	private final LocalDateTime endedAt;

	@QueryProjection
	public TodayLineupResponse(int waitingNumber, int numOfMember, ArrivalStatusEnum status, int callCount,
		LocalDateTime startedAt, LocalDateTime endedAt) {

		this.waitingNumber = waitingNumber;
		this.numOfMember = numOfMember;
		this.status = status;
		this.callCount = callCount;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
	}
}