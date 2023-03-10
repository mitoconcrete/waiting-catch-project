package team.waitingcatch.app.lineup.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.lineup.entity.LineupHistory;

public interface LineupHistoryRepository extends JpaRepository<LineupHistory, Long>, LineupHistoryRepositoryCustom {
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("update LineupHistory lh set lh.isReceivedReviewRequest = true, lh.modifiedDate = :now"
		+ " where lh.id in (:lineupHistoryIds)")
	void bulkUpdateIsReceivedReviewRequest(@Param("lineupHistoryIds") List<Long> ids, @Param("now") LocalDateTime now);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("update LineupHistory lh set lh.isDeleted = true where lh.restaurant.id = :restaurantId")
	void bulkSoftDeleteByRestaurantId(@Param("restaurantId") long restaurantId);
}