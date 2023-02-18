package team.waitingcatch.app.event.dto.event;

import lombok.Getter;

@Getter
public class DeleteEventServiceRequest {
	private Long eventId;
	private Long userId;

	public DeleteEventServiceRequest(Long eventId, Long userId) {
		this.eventId = eventId;
		this.userId = userId;
	}
}
