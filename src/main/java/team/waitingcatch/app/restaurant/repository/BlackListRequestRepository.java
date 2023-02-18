package team.waitingcatch.app.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.BlackListRequest;

public interface BlackListRequestRepository extends JpaRepository<BlackListRequest, Long> {
	List<BlackListRequest> findByUser_IdAndRestaurant_User_Id(Long userId,
		Long sellerId);

	@Query(value = "select b from BlackListRequest b where b.id =:user1 and b.restaurant.user.id =:seller1")
	Optional<BlackListRequest> findByIdAndRestaurantUserId(@Param("user1") Long userId,
		@Param("seller1") Long sellerId);

	@Query(value = "select b from BlackListRequest b where b.user.id =:user1 and b.restaurant.user.id =:seller11 "
		+ "and b.status = team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum.APPROVAL")
	BlackListRequest findByUser_IdAndRestaurant_User_IdAndStatus(@Param("user1") Long userId,
		@Param("seller11") Long sellerId);

}