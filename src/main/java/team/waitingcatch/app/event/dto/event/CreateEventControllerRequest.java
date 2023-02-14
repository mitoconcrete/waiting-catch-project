package team.waitingcatch.app.event.dto.event;

import lombok.Getter;

@Getter
public class CreateEventControllerRequest {

	private String name;
	private String eventStartDate;
	private String eventEndDate;
}
