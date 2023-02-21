package team.waitingcatch.app.restaurant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.BlackListRequest;

public interface BlackListRequestRepository extends JpaRepository<BlackListRequest, Long> {
	@Query("select br from BlackListRequest br join fetch br.restaurant where br.id = :blacklistRequestId")
	Optional<BlackListRequest> findByIdWithRestaurant(@Param("blacklistRequestId") Long id);
}