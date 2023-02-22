package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Blacklist;
import team.waitingcatch.app.restaurant.entity.BlacklistRequest;

@Getter
public class GetBlacklistResponse {
	private final Long userId;

	public GetBlacklistResponse(Blacklist blacklist) {
		this.userId = blacklist.getUser().getId();
	}
}