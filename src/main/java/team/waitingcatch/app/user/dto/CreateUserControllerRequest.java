package team.waitingcatch.app.user.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateUserControllerRequest {
	@NotNull
	@Min(value = 2, message = "이름은 최소 2글자 이상이어야합니다.")
	@Max(value = 5, message = "이름은 최대 5글자 이어야합니다.")
	private String name;

	@NotNull
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
		message = "이메일 형식을 맞춰주세요.")
	private String email;

	@NotNull
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{4,10}$",
		message = "4자 이상 10자 이내여야하며, 하나 이상의 알파벳 소문자와 숫자의 조합으로 이뤄져야 합니다.")
	private String username;

	@NotNull
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@!%*#?&])[A-Za-z0-9$@!%*#?&]{8,15}$",
		message = "8자 이상 15자 이내여야하며, 하나 이상의 알파벳, 숫자, 특수문자의 조합으로 이뤄져야 합니다.")
	private String password;

	@NotNull
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{4,10}$",
		message = "4자 이상 10자 이내여야하며, 하나 이상의 알파벳과 숫자의 조합으로 이뤄져야 합니다.")
	private String nickname;

	@NotNull
	@Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\\\d{3}|\\\\d{4})-\\\\d{4}$",
		message = "올바른 전화번호 형식을 입력하세요.")
	private String phoneNumber;
}