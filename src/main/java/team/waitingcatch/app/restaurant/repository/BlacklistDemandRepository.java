package team.waitingcatch.app.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.BlacklistDemand;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;

public interface BlacklistDemandRepository extends JpaRepository<BlacklistDemand, Long> {
	List<BlacklistDemand> findByUser_IdAndRestaurant_User_Id(Long userId, Long sellerId);

	@Query("select bd from BlacklistDemand bd where bd.user.id = :user and bd.restaurant.user.id = :seller "
		+ "and bd.status = team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum.APPROVE")
	Optional<BlacklistDemand> findByUser_IdAndRestaurant_User_IdAndStatusApproval(@Param("user") Long userId,
		@Param("seller") Long sellerId);

	@Query(value = "select bd from BlacklistDemand bd join fetch bd.user where bd.status = :status",
		countQuery = "select count(b) from BlacklistDemand b where b.status = :status")
	Page<BlacklistDemand> findAllByStatus(@Param("status") AcceptedStatusEnum status, Pageable pageable);

	@Query("select bd from BlacklistDemand bd join fetch bd.user where bd.restaurant.id = :restaurantId")
	List<BlacklistDemand> findAllByRestaurant_Id(@Param("restaurantId") Long restaurantId);
}