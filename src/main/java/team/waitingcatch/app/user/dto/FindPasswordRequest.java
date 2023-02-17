package team.waitingcatch.app.user.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindPasswordRequest {
	@NotNull(message = "이메일은 비어있을 수 없습니다.")
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
		message = "이메일 형식을 맞춰주세요.")
	private String email;

	@NotNull(message = "아이디는 비어있을 수 없습니다.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{4,10}$",
		message = "4자 이상 10자 이내여야하며, 하나 이상의 알파벳 소문자와 숫자의 조합으로 이뤄져야 합니다.")
	private String username;
}
