package team.waitingcatch.app.lineup.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LineupPriorityRedisRepository {
	private final RedisTemplate<String, ?> redisTemplate;

	public void add() {

	}
}