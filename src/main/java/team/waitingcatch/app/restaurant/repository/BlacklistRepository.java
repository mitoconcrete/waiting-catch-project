package team.waitingcatch.app.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.Blacklist;
import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
	@Query(value = "select b from Blacklist b join fetch b.user where b.restaurant.id = :restaurant")
	List<Blacklist> findAllByRestaurant(@Param("restaurant") Restaurant restaurant);

	@Query(value = "select b from Blacklist b join fetch b.user where b.restaurant.id = :restaurantId")
	List<Blacklist> findAllByRestaurant_Id(@Param("restaurantId") Long restaurantId);

	@Query(value = "select b from Blacklist b join fetch b.user where b.isDeleted =false",
		countQuery = "select count(b) from Blacklist b where b.isDeleted=false")
	Page<Blacklist> findAllByIsDeletedFalse(Pageable pageable);

	@Query("select b from Blacklist b where b.id = :black1 and b.restaurant.user.id = :seller1")
	Optional<Blacklist> findByIdAndRestaurantUserId(@Param("black1") Long blacklistId, @Param("seller1") Long sellerId);

	@Query("select b from Blacklist b where b.user.id = :userId and b.restaurant.user.id = :sellerId and b.isDeleted = false")
	Optional<Blacklist> findByUserIdAndRestaurantUserIdAndIsDeletedFalse(@Param("userId") Long userId,
		@Param("sellerId") Long sellerId);

	@Query("select b from Blacklist b where b.restaurant.id = :restaurantId and b.user.id = :userId and b.isDeleted = false")
	Optional<Blacklist> findByRestaurantIdAndUserIdAndIsDeletedFalse(@Param("restaurantId") Long restaurantId,
		@Param("userId") Long userId);
}