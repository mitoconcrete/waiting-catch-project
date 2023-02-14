package team.waitingcatch.app.redis.repository;

import org.springframework.data.repository.CrudRepository;

import team.waitingcatch.app.redis.entity.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
	RefreshToken findByToken(String token);

	boolean existsByUsername(String username);
}
