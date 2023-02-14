package team.waitingcatch.app.redis.service;

import team.waitingcatch.app.redis.dto.CreateRefreshTokenServiceRequest;

public interface RedisService {
	void createRefreshToken(CreateRefreshTokenServiceRequest payload);
}
