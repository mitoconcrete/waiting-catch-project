package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.BlacklistRequest;

@Getter
public class GetRequestBlacklistResponse {
	@NotNull
	private final Long userId;

	@NotNull
	private final String description;

	public GetRequestBlacklistResponse(BlacklistRequest blacklistRequest) {
		this.userId = blacklistRequest.getUser().getId();
		this.description = blacklistRequest.getDescription();
	}
}