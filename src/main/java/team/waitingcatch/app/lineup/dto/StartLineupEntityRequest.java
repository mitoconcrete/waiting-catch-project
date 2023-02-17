package team.waitingcatch.app.lineup.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;

@Getter
@RequiredArgsConstructor
public class StartLineupEntityRequest {
	private final User user;
	private final Restaurant restaurant;
	private final int waitingNumber;
	private final int numOfMembers;
	private final LocalDateTime startAt;

	public StartLineupEntityRequest(StartLineupServiceRequest serviceRequest, Restaurant restaurant, Integer lastWaitingNumber) {
		this.user = serviceRequest.getUser();
		this.restaurant = restaurant;
		this.waitingNumber = lastWaitingNumber != null ? lastWaitingNumber + 1 : 1;
		this.numOfMembers = serviceRequest.getNumOfMembers();
		this.startAt = serviceRequest.getStartAt();
	}
}