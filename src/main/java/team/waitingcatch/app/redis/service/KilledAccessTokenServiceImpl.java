package team.waitingcatch.app.redis.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.redis.dto.KillTokenRequest;
import team.waitingcatch.app.redis.entity.KilledAccessToken;
import team.waitingcatch.app.redis.repository.KilledAccessTokenRepository;

@Service
@RequiredArgsConstructor
public class KilledAccessTokenServiceImpl implements KilledAccessTokenService, InternalKillAccessTokenService {
	private final KilledAccessTokenRepository killedAccessTokenRepository;

	@Override
	public void killToken(KillTokenRequest payload) {
		KilledAccessToken killToken = new KilledAccessToken(payload.getAccessToken());
		killedAccessTokenRepository.save(killToken);
	}
}
