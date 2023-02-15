package team.waitingcatch.app.event.dto.event;

import lombok.Getter;

@Getter
public class DeleteEventControllerRequest {
	private Long eventId;

	public DeleteEventControllerRequest(Long eventId) {
		this.eventId = eventId;
	}
}
