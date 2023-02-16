package team.waitingcatch.app.redis.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RedisHash("killed-access-token")
@RequiredArgsConstructor
public class KilledAccessToken {
	@Id
	private final String token;
}
