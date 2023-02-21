package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.BlackListRequest;

@Getter
public class GetRequestBlackListResponse {
	@NotNull
	private final Long userId;
	@NotNull
	private final String description;

	public GetRequestBlackListResponse(BlackListRequest blackListRequest) {
		this.userId = blackListRequest.getUser().getId();
		this.description = blackListRequest.getDescription();
	}
}
