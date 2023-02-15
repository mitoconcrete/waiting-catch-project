package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateEventControllerRequest {
	private String name;
	private LocalDateTime eventStartDate;
	private LocalDateTime eventEndDate;
}
