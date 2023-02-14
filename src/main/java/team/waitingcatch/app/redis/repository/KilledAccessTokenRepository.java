package team.waitingcatch.app.redis.repository;

import org.springframework.data.repository.CrudRepository;

import team.waitingcatch.app.redis.entity.KilledAccessToken;

public interface KilledAccessTokenRepository extends CrudRepository<KilledAccessToken, String> {
}
