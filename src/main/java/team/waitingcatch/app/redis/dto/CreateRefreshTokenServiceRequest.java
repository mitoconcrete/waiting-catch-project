package team.waitingcatch.app.redis.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateRefreshTokenServiceRequest {
	private final String accessToken;
	private final String refreshToken;
	private final long timeToLive;
}
