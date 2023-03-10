package team.waitingcatch.app.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserControllerRequest {
	@NotBlank
	@Size(min = 2, max = 5, message = "이름은 최소 2글자에서 5글자 사이어야합니다.")
	private String name;

	@NotNull
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
		message = "올바른 형식을 입력하세요.")
	private String email;

	@NotNull
	@Pattern(regexp = "^(0[0-99]{1,2})-?([0-9]{3,4})-?([0-9]{4})$",
		message = "올바른 형식을 입력하세요.")
	private String phoneNumber;

	@Size(max = 10, message = "닉네임은 10자 이내여야합니다.")
	private String nickName;
}
