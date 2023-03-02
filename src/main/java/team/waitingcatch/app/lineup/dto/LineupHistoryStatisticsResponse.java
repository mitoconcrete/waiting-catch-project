package team.waitingcatch.app.lineup.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class LineupHistoryStatisticsResponse {
	private final String dayOfWeek;
	private final int avgWaitingNumber;
	private final int avgWaitingTime;

	@QueryProjection
	public LineupHistoryStatisticsResponse(String dayOfWeek, int avgWaitingNumber, int avgWaitingTime) {
		this.dayOfWeek = dayOfWeek;
		this.avgWaitingNumber = avgWaitingNumber;
		this.avgWaitingTime = avgWaitingTime;
	}
}