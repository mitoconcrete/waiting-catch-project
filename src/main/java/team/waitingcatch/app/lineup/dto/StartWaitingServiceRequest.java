package team.waitingcatch.app.lineup.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.user.entitiy.User;

@Getter
@RequiredArgsConstructor
public class StartWaitingServiceRequest {
	private final User user;
	private final long restaurantId;
	private final int numOfMembers;
	private final LocalDateTime startAt;
}