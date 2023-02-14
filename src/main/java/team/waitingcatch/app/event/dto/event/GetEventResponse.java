package team.waitingcatch.app.event.dto.event;

import lombok.Getter;
import team.waitingcatch.app.event.entity.Event;

@Getter
public class GetEventResponse {
	private String name;
	private String eventStartDate;
	private String eventEndDate;

	public GetEventResponse(Event event) {
		this.name = event.getName();
		this.eventStartDate = event.getEventStartDate();
		this.eventEndDate = event.getEventEndDate();
	}
}
