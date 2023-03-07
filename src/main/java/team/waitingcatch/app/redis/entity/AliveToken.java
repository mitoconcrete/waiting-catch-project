package team.waitingcatch.app.redis.entity;

import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RedisHash("refresh-token")
@RequiredArgsConstructor
public class AliveToken {
	@Id
	private final String accessToken;
	private final String refreshToken;
	@TimeToLive(unit = TimeUnit.MILLISECONDS)
	private final long expiration;
}
