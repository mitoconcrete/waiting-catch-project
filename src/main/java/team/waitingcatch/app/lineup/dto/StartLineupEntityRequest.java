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

	public StartLineupEntityRequest(StartWaitingServiceRequest serviceRequest, Restaurant restaurant,
		int waitingNumber) {

		this.user = serviceRequest.getUser();
		this.restaurant = restaurant;
		this.waitingNumber = waitingNumber;
		this.numOfMembers = serviceRequest.getNumOfMembers();
		this.startAt = serviceRequest.getStartAt();
	}
}