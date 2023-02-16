package team.waitingcatch.app.redis.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RemoveTokenRequest {
	private final String accessToken;
}
