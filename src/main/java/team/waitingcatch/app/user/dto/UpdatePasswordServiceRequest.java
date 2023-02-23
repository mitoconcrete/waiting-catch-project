package team.waitingcatch.app.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdatePasswordServiceRequest {
	private final String username;
	private final String password;
}
