package team.waitingcatch.app.lineup.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

@Getter
@RequiredArgsConstructor
public class UpdateArrivalStatusServiceRequest {
	private final long sellerId;
	private final long lineupId;
	private final ArrivalStatusEnum status;
}