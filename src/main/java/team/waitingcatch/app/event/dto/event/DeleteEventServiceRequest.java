package team.waitingcatch.app.event.dto.event;

import lombok.Getter;

@Getter
public class DeleteEventServiceRequest {
	private Long eventId;
	private String username;

	public DeleteEventServiceRequest(Long eventId, String username) {
		this.eventId = eventId;
		this.username = username;
	}
}
