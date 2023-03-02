package team.waitingcatch.app.redis.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.dto.service.UpdateTokenRequest;
import team.waitingcatch.app.exception.TokenNotFoundException;
import team.waitingcatch.app.redis.dto.CreateRefreshTokenServiceRequest;
import team.waitingcatch.app.redis.dto.GetRefreshTokenRequest;
import team.waitingcatch.app.redis.dto.GetRefreshTokenResponse;
import team.waitingcatch.app.redis.entity.AliveToken;
import team.waitingcatch.app.redis.repository.AliveTokenRepository;

@Service
@RequiredArgsConstructor
public class AliveTokenServiceImpl implements AliveTokenService, InternalAliveTokenService {
	private final AliveTokenRepository aliveTokenRepository;

	@Override
	public void createToken(CreateRefreshTokenServiceRequest payload) {
		AliveToken newAliveToken = new AliveToken(payload.getAccessToken(), payload.getRefreshToken(),
			payload.getTimeToLive());
		aliveTokenRepository.save(newAliveToken);
	}

	@Override
	public GetRefreshTokenResponse getRefreshToken(GetRefreshTokenRequest payload) {
		AliveToken aliveToken = aliveTokenRepository.findById(payload.getAccessToken()).orElseThrow(
			() -> new TokenNotFoundException("토큰이 존재하지 않습니다.")
		);
		return new GetRefreshTokenResponse(aliveToken.getRefreshToken());
	}

	@Override
	public void updateToken(UpdateTokenRequest payload) {
		RemoveTokenRequest removeServicePayload = new RemoveTokenRequest(payload.getOldAccessToken());
		CreateRefreshTokenServiceRequest createServicePayload = new CreateRefreshTokenServiceRequest(
			payload.getUpdateAccessToken(), payload.getRefreshToken(), payload.getTimeToLive());
		removeToken(removeServicePayload);
		createToken(createServicePayload);
	}

	@Override
	public void removeToken(RemoveTokenRequest payload) {
		AliveToken aliveToken = _getAliveTokenByAccessToken(payload.getAccessToken());
		aliveTokenRepository.delete(aliveToken);
	}

	@Override
	public AliveToken _getAliveTokenByAccessToken(String AccessToken) {
		return aliveTokenRepository.findById(AccessToken).orElseThrow(
			() -> new TokenNotFoundException("토큰이 존재하지 않습니다.")
		);
	}
}
