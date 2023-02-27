package team.waitingcatch.app.redis.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.JwtUtil;
import team.waitingcatch.app.redis.dto.KillTokenRequest;
import team.waitingcatch.app.redis.dto.ValidateTokenRequest;
import team.waitingcatch.app.redis.entity.KilledAccessToken;
import team.waitingcatch.app.redis.repository.KilledAccessTokenRepository;

@Service
@RequiredArgsConstructor
public class KilledAccessTokenServiceImpl implements KilledAccessTokenService, InternalKillAccessTokenService {
	private final KilledAccessTokenRepository killedAccessTokenRepository;
	private final JwtUtil jwtUtil;

	@Override
	public void killToken(KillTokenRequest payload) {
		/* 만료시간까지 남은 시간을 측정해서 TTL로 넣어준다.
	 	만료시간 이후로 해당 토큰은 사용할 수 없기에 블랙리스트에서 제거해도 무방함. */
		long remainExpiration =
			jwtUtil.getTokenClaims(payload.getAccessToken()).getExpiration().getTime() - System.currentTimeMillis();
		KilledAccessToken killToken = new KilledAccessToken(payload.getAccessToken(), remainExpiration);
		killedAccessTokenRepository.save(killToken);
	}

	@Override
	public void validateToken(ValidateTokenRequest payload) {
		killedAccessTokenRepository.findById(payload.getAccessToken()).ifPresent(token -> {
				throw new IllegalArgumentException("이미 사용된 토큰입니다.");
			}
		);
	}
}
