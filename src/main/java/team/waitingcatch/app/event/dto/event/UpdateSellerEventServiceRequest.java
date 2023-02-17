package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateSellerEventServiceRequest {
	@NotNull
	@Size(min = 2, max = 20, message = "이벤트 이름은 최소 2글자에서 5글자 사이어야합니다.")
	private final String name;

	@NotNull
	@Future
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} [0-9]{2}:[0-9]{2}$", message = "YYYY-MM-DD HH:MM 형식으로 입력해주세요")
	private final LocalDateTime eventStartDate;

	@NotNull
	@Future
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} [0-9]{2}:[0-9]{2}$", message = "YYYY-MM-DD HH:MM 형식으로 입력해주세요")
	private final LocalDateTime eventEndDate;

	private final Long eventId;

	private final Long userId;

	public UpdateSellerEventServiceRequest(UpdateEventControllerRequest updateEventControllerRequest, Long eventId,
		Long userId) {
		this.name = updateEventControllerRequest.getName();
		this.eventStartDate = updateEventControllerRequest.getEventStartDate();
		this.eventEndDate = updateEventControllerRequest.getEventEndDate();
		this.eventId = eventId;
		this.userId = userId;
	}
}
