package team.waitingcatch.app.lineup.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

@Getter
@RequiredArgsConstructor
public class GetLineupRecordsServiceRequest {
	private final long userId;
	private final ArrivalStatusEnum status;
}