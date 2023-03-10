package team.waitingcatch.app.redis.repository;

import org.springframework.data.repository.CrudRepository;

import team.waitingcatch.app.redis.entity.ValidCode;

public interface ValidCodeRepository extends CrudRepository<ValidCode, String> {
}
