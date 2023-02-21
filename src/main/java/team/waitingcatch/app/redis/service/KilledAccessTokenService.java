package team.waitingcatch.app.redis.service;

import team.waitingcatch.app.redis.dto.KillTokenRequest;
import team.waitingcatch.app.redis.dto.ValidateTokenRequest;

public interface KilledAccessTokenService {
	void killToken(KillTokenRequest payload);

	void validateToken(ValidateTokenRequest payload);
}
