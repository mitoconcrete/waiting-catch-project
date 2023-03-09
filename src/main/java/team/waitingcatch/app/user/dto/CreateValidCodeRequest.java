package team.waitingcatch.app.user.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateValidCodeRequest {
	@NotNull
	@Pattern(regexp = "^(0[0-99]{1,2})-?([0-9]{3,4})-?([0-9]{4})$",
		message = "올바른 형식을 입력하세요.")
	private String phoneNumber;
}
