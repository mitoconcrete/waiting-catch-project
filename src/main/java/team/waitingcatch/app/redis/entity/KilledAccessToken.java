package team.waitingcatch.app.redis.entity;

import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RedisHash("killed-access-token")
@RequiredArgsConstructor
public class KilledAccessToken {
	@Id
	private final String token;

	@TimeToLive(unit = TimeUnit.MILLISECONDS)
	private final long expiration;
}
