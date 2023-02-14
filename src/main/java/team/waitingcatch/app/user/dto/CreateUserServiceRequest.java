package team.waitingcatch.app.user.dto;

import lombok.Getter;
import team.waitingcatch.app.user.enums.UserRoleEnum;

@Getter
public class CreateUserServiceRequest {
	private final String name;
	private final String email;
	private final String username;
	private final String password;
	private final String nickname;
	private final String phoneNumber;
	private final UserRoleEnum role;

	public CreateUserServiceRequest(UserRoleEnum role, String name, String email, String username, String password,
		String nickname,
		String phoneNumber) {
		this.role = role;
		this.name = name;
		this.email = email;
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
	}
}
