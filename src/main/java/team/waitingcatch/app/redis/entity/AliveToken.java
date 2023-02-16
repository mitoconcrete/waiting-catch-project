package team.waitingcatch.app.redis.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RedisHash("refresh-token")
@RequiredArgsConstructor
public class AliveToken {
	@Id
	private final String accessToken;
	private final String refreshToken;
}
