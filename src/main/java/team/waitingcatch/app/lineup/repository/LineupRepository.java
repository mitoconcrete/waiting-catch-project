package team.waitingcatch.app.lineup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.lineup.entity.Lineup;

public interface LineupRepository extends JpaRepository<Lineup, Long> {
	@Query("select max(l.waitingNumber) from Lineup l where l.restaurant.id = :restaurantId")
	Integer findLastWaitingNumberByRestaurantId(@Param("restaurantId") Long id);
}