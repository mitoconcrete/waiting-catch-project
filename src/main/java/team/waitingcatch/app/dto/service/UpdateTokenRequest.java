package team.waitingcatch.app.dto.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateTokenRequest {
	private final String oldAccessToken;
	private final String updateAccessToken;
	private final String refreshToken;
	private final long timeToLive;
}
