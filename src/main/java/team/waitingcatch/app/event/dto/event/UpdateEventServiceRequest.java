package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateEventServiceRequest {
	private String name;
	private LocalDateTime eventStartDate;
	private LocalDateTime eventEndDate;
	private Long eventId;

	public UpdateEventServiceRequest(UpdateEventControllerRequest updateEventControllerRequest, Long eventId) {
		this.name = updateEventControllerRequest.getName();
		this.eventStartDate = updateEventControllerRequest.getEventStartDate();
		this.eventEndDate = updateEventControllerRequest.getEventEndDate();
		this.eventId = eventId;
	}
}
