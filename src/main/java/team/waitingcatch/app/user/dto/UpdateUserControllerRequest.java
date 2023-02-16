package team.waitingcatch.app.user.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserControllerRequest {
	@NotNull
	@Size(min = 2, max = 5, message = "이름은 최소 2글자에서 5글자 사이어야합니다.")
	private String name;

	@NotNull
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
		message = "이메일 형식을 맞춰주세요.")
	private String email;

	@NotNull
	@Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$",
		message = "올바른 전화번호 형식을 입력하세요.")
	private String phoneNumber;

	@NotNull
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{4,10}$",
		message = "4자 이상 10자 이내여야하며, 하나 이상의 알파벳과 숫자의 조합으로 이뤄져야 합니다.")
	private String nickName;
}
