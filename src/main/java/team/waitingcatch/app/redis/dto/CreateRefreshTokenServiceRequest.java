package team.waitingcatch.app.redis.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateRefreshTokenServiceRequest {
	private final String username;
	private final String refreshToken;
}
