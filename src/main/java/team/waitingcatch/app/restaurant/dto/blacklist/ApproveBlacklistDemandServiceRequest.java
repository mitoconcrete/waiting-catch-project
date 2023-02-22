package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;

@Getter
public class ApproveBlacklistDemandServiceRequest {
	private final Long blacklistDemandId;

	public ApproveBlacklistDemandServiceRequest(Long blacklistDemandId) {
		this.blacklistDemandId = blacklistDemandId;
	}
}