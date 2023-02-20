package team.waitingcatch.app.lineup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.lineup.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
	@Modifying
	@Query(value = "update Review r set r.is_deleted=true where r.restaurant_id = :restaurantId", nativeQuery = true)
	void bulkSoftDeleteByRestaurantId(@Param("restaurantId") Long restaurantId);
}