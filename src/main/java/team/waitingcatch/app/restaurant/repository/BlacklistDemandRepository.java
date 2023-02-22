package team.waitingcatch.app.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.BlacklistDemand;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;

public interface BlacklistDemandRepository extends JpaRepository<BlacklistDemand, Long> {
	List<BlacklistDemand> findByUser_IdAndRestaurant_User_Id(Long userId,
		Long sellerId);

	@Query(value = "select bd from BlacklistDemand bd where bd.user.id =:user1 and bd.restaurant.user.id =:seller1")
	Optional<BlacklistDemand> findByIdAndRestaurantUserId(@Param("user1") Long userId,
		@Param("seller1") Long sellerId);

	@Query(value = "select bd from BlacklistDemand bd where bd.user.id =:user1 and bd.restaurant.user.id =:seller11 "
		+ "and bd.status = team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum.APPROVE")
	Optional<BlacklistDemand> findByUser_IdAndRestaurant_User_IdAndStatusApproval(@Param("user1") Long userId,
		@Param("seller11") Long sellerId);

	@Query(value = "select bd from BlacklistDemand bd where bd.status = :status")
	List<BlacklistDemand> findAllByStatus(@Param("status") AcceptedStatusEnum status);

	List<BlacklistDemand> findAllByRestaurant_Id(Long restaurantId);
}