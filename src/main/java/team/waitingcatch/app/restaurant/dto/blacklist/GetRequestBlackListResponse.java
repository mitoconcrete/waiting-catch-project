package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.BlackListRequest;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;

@Getter
public class GetRequestBlackListResponse {
	private final Long id;
	@NotNull
	private final Long userId;
	@NotNull
	private final String description;

	private final AcceptedStatusEnum status;

	public GetRequestBlackListResponse(BlackListRequest blackListRequest) {
		this.id = blackListRequest.getId();
		this.userId = blackListRequest.getUser().getId();
		this.description = blackListRequest.getDescription();
		this.status = blackListRequest.getStatus();
	}
}
