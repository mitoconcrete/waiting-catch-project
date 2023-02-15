package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateSellerEventServiceRequest {
	private final String name;
	private final LocalDateTime eventStartDate;
	private final LocalDateTime eventEndDate;
	private final Long eventId;
	private final String username;

	public UpdateSellerEventServiceRequest(UpdateEventControllerRequest updateEventControllerRequest, Long eventId,
		String username) {
		this.name = updateEventControllerRequest.getName();
		this.eventStartDate = updateEventControllerRequest.getEventStartDate();
		this.eventEndDate = updateEventControllerRequest.getEventEndDate();
		this.eventId = eventId;
		this.username = username;
	}
}
