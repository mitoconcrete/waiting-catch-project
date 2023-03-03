package team.waitingcatch.app.lineup.repository;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.lineup.entity.LineupHistory;

public interface LineupHistoryRepository extends JpaRepository<LineupHistory, Long>, LineupHistoryRepositoryCustom {
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("update LineupHistory l set l.isDeleted = true where l.restaurant.id = :restaurantId")
	void bulkSoftDeleteByRestaurantId(@Param("restaurantId") Long restaurantId);
}