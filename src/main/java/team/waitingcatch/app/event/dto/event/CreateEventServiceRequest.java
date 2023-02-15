package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CreateEventServiceRequest {
	private String name;
	private LocalDateTime eventStartDate;
	private LocalDateTime eventEndDate;
	private Long restaurantId;

	public CreateEventServiceRequest(CreateEventControllerRequest createEventControllerRequest, Long restaurantId) {
		this.name = createEventControllerRequest.getName();
		this.eventStartDate = createEventControllerRequest.getEventStartDate();
		this.eventEndDate = createEventControllerRequest.getEventEndDate();
		this.restaurantId = restaurantId;
	}
}
