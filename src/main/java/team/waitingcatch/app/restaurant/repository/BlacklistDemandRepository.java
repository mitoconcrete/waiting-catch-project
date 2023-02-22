package team.waitingcatch.app.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.BlacklistRequest;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;

public interface BlacklistRequestRepository extends JpaRepository<BlacklistRequest, Long> {
	List<BlacklistRequest> findByUser_IdAndRestaurant_User_Id(Long userId,
		Long sellerId);

	@Query(value = "select b from BlacklistRequest b where b.user.id =:user1 and b.restaurant.user.id =:seller1")
	Optional<BlacklistRequest> findByIdAndRestaurantUserId(@Param("user1") Long userId,
		@Param("seller1") Long sellerId);

	@Query(value = "select b from BlacklistRequest b where b.user.id =:user1 and b.restaurant.user.id =:seller11 "
		+ "and b.status = team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum.APPROVAL")
	Optional<BlacklistRequest> findByUser_IdAndRestaurant_User_IdAndStatusApproval(@Param("user1") Long userId,
		@Param("seller11") Long sellerId);

	@Query(value = "select b from BlacklistRequest b where b.status = :status")
	List<BlacklistRequest> findAllByStatus(@Param("status") AcceptedStatusEnum status);
}