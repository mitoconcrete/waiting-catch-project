package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class ApproveBlackListServiceRequest {
	@NotNull
	private final Long blackListRequestId;

	public ApproveBlackListServiceRequest(Long blackListRequestId) {
		this.blackListRequestId = blackListRequestId;
	}
}
