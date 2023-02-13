package team.waitingcatch.app.event.dto.event;

import lombok.Getter;

@Getter
public class UpdateEventServiceRequest {
	private String name;
	private String eventStartDate;
	private String eventEndDate;
	private Long eventId;

	public UpdateEventServiceRequest(UpdateEventControllerRequest updateEventControllerRequest, Long eventId) {
		this.name = updateEventControllerRequest.getName();
		this.eventStartDate = updateEventControllerRequest.getEventStartDate();
		this.eventEndDate = updateEventControllerRequest.getEventEndDate();
		this.eventId = eventId;
	}
}
