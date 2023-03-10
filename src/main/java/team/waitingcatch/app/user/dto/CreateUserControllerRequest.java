package team.waitingcatch.app.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class CreateUserControllerRequest {
	@NotBlank
	@Size(min = 2, max = 5, message = "최소 2글자에서 5글자 사이어야합니다.")
	private String name;

	@NotNull
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
		message = "올바른 형식을 입력하세요.")
	private String email;

	@NotNull
	@Pattern(regexp = "^(?=.*[a-zA-Z0-9])[a-zA-Z0-9]{4,15}$",
		message = "4자 이상 15자 이내여야하며, 하나 이상의 알파벳 소문자와 숫자의 조합으로 이뤄져야 합니다.")
	private String username;

	@NotNull
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@!%*#?&\\-])[A-Za-z0-9$@!%*#?&]{8,15}$",
		message = "8자 이상 15자 이내여야하며, 하나 이상의 알파벳, 숫자, 특수문자의 조합으로 이뤄져야 합니다.")
	private String password;

	@NotNull
	@Size(min = 4, max = 10, message = "4자 이상 10자 이내여야합니다.")
	private String nickname;

	@NotNull
	@Pattern(regexp = "^(0[0-99]{1,2})-?([0-9]{3,4})-?([0-9]{4})$",
		message = "올바른 형식을 입력하세요.")
	private String phoneNumber;
}