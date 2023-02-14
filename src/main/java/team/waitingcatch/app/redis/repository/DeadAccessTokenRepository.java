package team.waitingcatch.app.redis.repository;

import org.springframework.data.repository.CrudRepository;

import team.waitingcatch.app.redis.entity.DeadAccessToken;

public interface DeadAccessTokenRepository extends CrudRepository<DeadAccessToken, Long> {
	DeadAccessToken findByToken(String token);
}
