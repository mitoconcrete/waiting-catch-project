package team.waitingcatch.app.dto.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateTokenRequest {
	private final String accessToken;
	private final String refreshToken;
}
