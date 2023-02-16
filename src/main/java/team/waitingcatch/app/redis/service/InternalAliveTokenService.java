package team.waitingcatch.app.redis.service;

import team.waitingcatch.app.redis.entity.AliveToken;

public interface InternalAliveTokenService {
	AliveToken _getAliveTokenByAccessToken(String AccessToken);
}
