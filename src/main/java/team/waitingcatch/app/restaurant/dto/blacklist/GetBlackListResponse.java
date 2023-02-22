package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.BlackList;

@Getter
public class GetBlackListResponse {
	private final Long Id;
	private final Long userId;

	private final String userName;
	private final String name;

	private final boolean isDeleted;

	public GetBlackListResponse(BlackList blackList) {
		this.Id = blackList.getId();
		this.userId = blackList.getUser().getId();
		this.userName = blackList.getUser().getUsername();
		this.name = blackList.getUser().getName();
		this.isDeleted = blackList.isDeleted();
	}
}
