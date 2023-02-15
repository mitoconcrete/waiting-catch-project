package team.waitingcatch.app.event.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.restaurant.entity.Restaurant;

@Getter
@NoArgsConstructor
public class CreateEventRequest {
	private CreateEventServiceRequest createEventServiceRequest;
	private Restaurant restaurant;

	public CreateEventRequest(CreateEventServiceRequest createEventServiceRequest,
		Restaurant restaurant) {
		this.createEventServiceRequest = createEventServiceRequest;
		this.restaurant = restaurant;
	}
}
