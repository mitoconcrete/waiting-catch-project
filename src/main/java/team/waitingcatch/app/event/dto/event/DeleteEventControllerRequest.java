package team.waitingcatch.app.event.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteEventControllerRequest {
	private Long eventId;

	public DeleteEventControllerRequest(Long eventId) {
		this.eventId = eventId;
	}
}
