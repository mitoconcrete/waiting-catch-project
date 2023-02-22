package team.waitingcatch.app.restaurant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.BlacklistRequest;

public interface BlacklistRequestRepository extends JpaRepository<BlacklistRequest, Long> {
	@Query("select br from BlacklistRequest br join fetch br.restaurant where br.id = :blacklistRequestId")
	Optional<BlacklistRequest> findByIdWithRestaurant(@Param("blacklistRequestId") Long id);

	Optional<BlacklistRequest> findByUser_IdAndRestaurant_User_Username(Long userId, String username);
}