package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateEventServiceRequest {
	private final String name;
	private final LocalDateTime eventStartDate;
	private final LocalDateTime eventEndDate;
	private final Long eventId;

	public UpdateEventServiceRequest(UpdateEventControllerRequest updateEventControllerRequest, Long eventId) {
		this.name = updateEventControllerRequest.getName();
		this.eventStartDate = updateEventControllerRequest.getEventStartDate();
		this.eventEndDate = updateEventControllerRequest.getEventEndDate();
		this.eventId = eventId;
	}
}
