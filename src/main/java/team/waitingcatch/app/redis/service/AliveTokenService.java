package team.waitingcatch.app.redis.service;

import team.waitingcatch.app.dto.service.UpdateTokenRequest;
import team.waitingcatch.app.redis.dto.CreateRefreshTokenServiceRequest;
import team.waitingcatch.app.redis.dto.GetRefreshTokenRequest;
import team.waitingcatch.app.redis.dto.GetRefreshTokenResponse;

public interface AliveTokenService {
	void createToken(CreateRefreshTokenServiceRequest payload);

	GetRefreshTokenResponse getRefreshToken(GetRefreshTokenRequest payload);

	void updateToken(UpdateTokenRequest payload);

	void removeToken(RemoveTokenRequest payload);
}
