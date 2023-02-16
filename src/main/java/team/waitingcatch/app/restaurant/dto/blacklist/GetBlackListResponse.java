package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.BlackList;

@Getter
public class GetBlackListResponse {
	@NotNull
	private final Long userId;

	public GetBlackListResponse(BlackList blackList) {
		this.userId = blackList.getUser().getId();
	}
}
