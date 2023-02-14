package team.waitingcatch.app.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateUserServiceRequest {
	private final String name;
	private final String email;
	private final String username;
	private final String nickName;
	private final String phoneNumber;
}
