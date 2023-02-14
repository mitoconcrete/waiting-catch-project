package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class ApproveBlackListServiceRequest {
	private final Long blackListRequestId;

	public ApproveBlackListServiceRequest(Long blackListRequestId) {
		this.blackListRequestId = blackListRequestId;
	}
}
