package team.waitingcatch.app.event.dto.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteEventServiceRequest {
	private Long eventId;
	private Long userId;

	public DeleteEventServiceRequest(Long eventId, Long userId) {
		this.eventId = eventId;
		this.userId = userId;
	}
}
