package team.waitingcatch.app.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.BlackListRequest;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;

public interface BlackListRequestRepository extends JpaRepository<BlackListRequest, Long> {
	Optional<BlackListRequest> findByUser_IdAndRestaurant_User_Username(Long userId, String username);

	@Query(value = "select b from BlackListRequest b where b.status = :status")
	List<BlackListRequest> findAllByStatus(@Param("status") AcceptedStatusEnum status);

}