package team.waitingcatch.app.redis.service;

import team.waitingcatch.app.redis.dto.KillTokenRequest;

public interface KilledAccessTokenService {
	void killToken(KillTokenRequest payload);
}
