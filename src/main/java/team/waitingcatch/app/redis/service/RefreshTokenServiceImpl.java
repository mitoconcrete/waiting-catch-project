package team.waitingcatch.app.redis.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.redis.dto.CreateRefreshTokenServiceRequest;
import team.waitingcatch.app.redis.entity.RefreshToken;
import team.waitingcatch.app.redis.repository.RefreshTokenRepository;

@Component
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService, InternalRefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public void createToken(CreateRefreshTokenServiceRequest payload) {
		RefreshToken newRefreshToken = new RefreshToken(payload.getUsername(), payload.getRefreshToken());
		refreshTokenRepository.save(newRefreshToken);
	}
}
