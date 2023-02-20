package team.waitingcatch.app.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.BlackList;
import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {

	@Query(value = "select b from BlackList b join fetch  b.user where b.restaurant.id =:res1")
	List<BlackList> findAllByRestaurant(@Param("res1") Restaurant restaurant);

	@Query(value = "select b from BlackList b where b.id =:black1 and b.restaurant.user.id =:seller1")
	Optional<BlackList> findByIdAndRestaurantUserId(@Param("black1") Long blacklistId, @Param("seller1") Long sellerId);

	@Query(value = "select b from BlackList b where b.id =:user1 and b.restaurant.user.id =:seller1 and b.isDeleted =false")
	Optional<BlackList> findByUserIdAndRestaurantUserIdAndIsDeletedFalse(@Param("user1") Long userId,
		@Param("seller1") Long sellerId);
}