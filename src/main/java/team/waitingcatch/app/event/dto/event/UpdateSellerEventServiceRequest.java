package team.waitingcatch.app.event.dto.event;

import lombok.Getter;

@Getter
public class UpdateSellerEventServiceRequest {
	private String name;
	private String eventStartDate;
	private String eventEndDate;
	private Long eventId;
	private String username;

	public UpdateSellerEventServiceRequest(UpdateEventControllerRequest updateEventControllerRequest, Long eventId,
		String username) {
		this.name = updateEventControllerRequest.getName();
		this.eventStartDate = updateEventControllerRequest.getEventStartDate();
		this.eventEndDate = updateEventControllerRequest.getEventEndDate();
		this.eventId = eventId;
		this.username = username;
	}
}
