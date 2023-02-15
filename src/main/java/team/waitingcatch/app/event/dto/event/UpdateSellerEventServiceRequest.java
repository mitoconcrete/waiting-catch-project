package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateSellerEventServiceRequest {
	private String name;
	private LocalDateTime eventStartDate;
	private LocalDateTime eventEndDate;
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
