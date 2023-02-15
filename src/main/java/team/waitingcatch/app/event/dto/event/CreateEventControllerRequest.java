package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateEventControllerRequest {

	private final String name;
	private final LocalDateTime eventStartDate;
	private final LocalDateTime eventEndDate;
}
