package team.waitingcatch.app.event.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateEventControllerRequest {
	private String name;
	private String eventStartDate;
	private String eventEndDate;
}
