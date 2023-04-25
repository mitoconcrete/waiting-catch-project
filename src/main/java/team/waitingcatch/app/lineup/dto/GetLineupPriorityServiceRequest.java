package team.waitingcatch.app.lineup.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetLineupPriorityServiceRequest {
	private final long restaurantId;
	private final long userId;
}