package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateEventControllerRequest {
	@NotBlank(message = "이벤트 이름을 입력하세요.")
	@Size(min = 2, max = 20, message = "이벤트 이름은 최소 2글자에서 20글자 사이어야합니다.")
	private String name;

	@NotNull(message = "시작일자를 입력하세요.")
	@Future
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime eventStartDate;

	@NotNull(message = "종료일자를 입력하세요.")
	@Future
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime eventEndDate;
}