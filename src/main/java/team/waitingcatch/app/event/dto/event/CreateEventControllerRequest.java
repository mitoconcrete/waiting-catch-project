package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateEventControllerRequest {

	private String name;
	private LocalDateTime eventStartDate;
	private LocalDateTime eventEndDate;
}
