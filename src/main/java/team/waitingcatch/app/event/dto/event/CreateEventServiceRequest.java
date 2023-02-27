package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;

@Getter
public class CreateEventServiceRequest {
	@NotNull
	@Size(min = 2, max = 20, message = "이벤트 이름은 최소 2글자에서 5글자 사이어야합니다.")
	private String name;

	@NotNull
	@Future
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	//@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} [0-9]{2}:[0-9]{2}$", message = "YYYY-MM-DD HH:MM 형식으로 입력해주세요")
	private LocalDateTime eventStartDate;

	@NotNull
	@Future
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	//@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} [0-9]{2}:[0-9]{2}$", message = "YYYY-MM-DD HH:MM 형식으로 입력해주세요")
	private LocalDateTime eventEndDate;

	private Long restaurantId;

	public CreateEventServiceRequest(CreateEventControllerRequest createEventControllerRequest, Long restaurantId) {
		this.name = createEventControllerRequest.getName();
		this.eventStartDate = createEventControllerRequest.getEventStartDate();
		this.eventEndDate = createEventControllerRequest.getEventEndDate();
		this.restaurantId = restaurantId;
	}
}
