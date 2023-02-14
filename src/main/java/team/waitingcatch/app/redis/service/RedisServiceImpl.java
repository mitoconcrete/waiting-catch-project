package team.waitingcatch.app.redis.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.redis.dto.CreateRefreshTokenServiceRequest;
import team.waitingcatch.app.redis.entity.RefreshToken;
import team.waitingcatch.app.redis.repository.DeadAccessTokenRepository;
import team.waitingcatch.app.redis.repository.RefreshTokenRepository;

@Component
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService, InternalRedisService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final DeadAccessTokenRepository deadAccessTokenRepository;

	@Override
	public void createRefreshToken(CreateRefreshTokenServiceRequest payload) {
		RefreshToken newRefreshToken = new RefreshToken(payload.getUsername(), payload.getRefreshToken());
		refreshTokenRepository.save(newRefreshToken);
	}
}
