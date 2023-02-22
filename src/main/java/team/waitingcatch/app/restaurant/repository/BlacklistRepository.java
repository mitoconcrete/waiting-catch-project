package team.waitingcatch.app.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.Blacklist;
import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
	@Query(value = "select b from Blacklist b join fetch b.user where b.restaurant.id = :res1")
	List<Blacklist> findAllByRestaurant(@Param("res1") Restaurant restaurant);

	List<Blacklist> findAllByRestaurant_Id(Long restaurantId);

	List<Blacklist> findAllByIsDeletedFalse();

	@Query(value = "select b from Blacklist b where b.id = :black1 and b.restaurant.user.id = :seller1")
	Optional<Blacklist> findByIdAndRestaurantUserId(@Param("black1") Long blacklistId, @Param("seller1") Long sellerId);

	@Query(value = "select b from Blacklist b where b.user.id = :user1 and b.restaurant.user.id = :seller1 and b.isDeleted = false")
	Optional<Blacklist> findByUserIdAndRestaurantUserIdAndIsDeletedFalse(@Param("user1") Long userId,
		@Param("seller1") Long sellerId);
}