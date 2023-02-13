package team.waitingcatch.app.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCreateControllerRequest {
	private String name;
	private String email;
	private String username;
	private String password;
	private String nickname;
	private String phoneNumber;
}
