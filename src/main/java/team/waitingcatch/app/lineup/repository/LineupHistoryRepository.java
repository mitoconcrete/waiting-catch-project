package team.waitingcatch.app.lineup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.lineup.entity.LineupHistory;

public interface LineupHistoryRepository extends JpaRepository<LineupHistory, Long>, LineupHistoryRepositoryCustom {
	@Modifying
	@Query(value = "update LineupHistory l set l.is_deleted = true where l.restaurant_id = :restaurantId", nativeQuery = true)
	void bulkSoftDeleteByRestaurantId(@Param("restaurantId") Long restaurantId);
}