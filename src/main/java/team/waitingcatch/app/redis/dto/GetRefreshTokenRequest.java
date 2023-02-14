package team.waitingcatch.app.redis.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetRefreshTokenRequest {
	private final String accessToken;
}
