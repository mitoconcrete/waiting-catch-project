package team.waitingcatch.app.redis.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RedisHash("old-access-token")
@RequiredArgsConstructor
public class DeadAccessToken {
	@Id
	private final String username;
	private final String token;

}
