package team.waitingcatch.app.redis.repository;

import org.springframework.data.repository.CrudRepository;

import team.waitingcatch.app.redis.entity.AliveToken;

public interface AliveTokenRepository extends CrudRepository<AliveToken, String> {
}
