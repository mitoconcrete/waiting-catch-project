package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.event.entity.Event;

@Getter
@NoArgsConstructor
public class GetEventResponse {
	private String name;
	private LocalDateTime eventStartDate;
	private LocalDateTime eventEndDate;

	public GetEventResponse(Event event) {
		this.name = event.getName();
		this.eventStartDate = event.getEventStartDate();
		this.eventEndDate = event.getEventEndDate();
	}
}
