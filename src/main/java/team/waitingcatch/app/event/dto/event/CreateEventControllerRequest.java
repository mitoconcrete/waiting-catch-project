package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateEventControllerRequest {
	@NotBlank
	@Size(min = 2, max = 20, message = "이벤트 이름은 최소 2글자에서 5글자 사이어야합니다.")
	private String name;

	@NotNull
	//@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} [0-9]{2}:[0-9]{2}$", message = "YYYY-MM-DD HH:MM 형식으로 입력해주세요")
	private String eventStartDate;

	@NotNull
	//@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} [0-9]{2}:[0-9]{2}$", message = "YYYY-MM-DD HH:MM 형식으로 입력해주세요")
	private String eventEndDate;

	public LocalDateTime getEventStartDate() {
		return LocalDateTime.parse(eventStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
	}

	public void setEventStartDate(String eventStartDate) {
		this.eventStartDate = eventStartDate;
	}

	public LocalDateTime getEventEndDate() {
		return LocalDateTime.parse(eventEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
	}

	public void setEventEndDate(String eventEndDate) {
		this.eventEndDate = eventEndDate;
	}
}
