package team.waitingcatch.app.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserControllerRequest {
	private String name;
	private String email;
	private String phoneNumber;
	private String nickName;
}
