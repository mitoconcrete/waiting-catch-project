package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;

import lombok.Getter;
import team.waitingcatch.app.event.entity.Event;

@Getter
public class GetEventsResponse {
	private Long id;
	private String name;
	private LocalDateTime eventStartDate;
	private LocalDateTime eventEndDate;

	public GetEventsResponse(Event event) {
		this.id = event.getId();
		this.name = event.getName();
		this.eventStartDate = event.getEventStartDate();
		this.eventEndDate = event.getEventEndDate();
	}
}
