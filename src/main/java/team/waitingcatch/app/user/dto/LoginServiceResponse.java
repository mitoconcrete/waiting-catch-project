package team.waitingcatch.app.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginServiceResponse {
	private final String accessToken;
}
