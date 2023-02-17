package team.waitingcatch.app.lineup.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateArrivalStatusServiceRequest {
	private Long sellerId;
	private Long lineupId;
	private ArrivalStatusEnum status;

	public UpdateArrivalStatusServiceRequest(Long sellerId, Long lineupId, ArrivalStatusEnum status) {
		this.sellerId = sellerId;
		this.lineupId = lineupId;
		this.status = status;
	}
}