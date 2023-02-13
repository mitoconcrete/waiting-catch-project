package team.waitingcatch.app.event.dto.event;

import lombok.Getter;

@Getter
public class UpdateEventControllerRequest {
	private String name;
	private String eventStartDate;
	private String eventEndDate;
}
