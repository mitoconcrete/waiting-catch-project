package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;

@Getter
@RequiredArgsConstructor
public class BlacklistDemandResponse {
	private final long blackListRequestId;
	private final String username;
	private final AcceptedStatusEnum status;
	private final String description;
}