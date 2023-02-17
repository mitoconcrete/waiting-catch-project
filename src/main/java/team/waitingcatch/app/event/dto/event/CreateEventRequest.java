package team.waitingcatch.app.event.dto.event;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Restaurant;

@Getter
public class CreateEventRequest {
	private CreateEventServiceRequest createEventServiceRequest;
	private Restaurant restaurant;

	public CreateEventRequest(CreateEventServiceRequest createEventServiceRequest,
		Restaurant restaurant) {
		this.createEventServiceRequest = createEventServiceRequest;
		this.restaurant = restaurant;
	}
}
