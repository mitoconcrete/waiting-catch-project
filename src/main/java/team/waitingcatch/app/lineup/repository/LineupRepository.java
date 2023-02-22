package team.waitingcatch.app.lineup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.lineup.entity.Lineup;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

public interface LineupRepository extends JpaRepository<Lineup, Long>, LineupRepositoryCustom {
	@Query("select l from Lineup l where l.user.id = :userId")
	List<Lineup> findAllByUserId(@Param("userId") Long userId);

	@Query("select l from Lineup l join fetch l.user where l.id = :lineupId")
	Optional<Lineup> findByIdWithUser(@Param("lineupId") Long id);

	@Query("select l.restaurant.id from Lineup l")
	Optional<Long> findRestaurantIdById(@Param("lineupId") Long id);

	@Query("select l from Lineup l where l.user.id = :userId and l.restaurant.id = :restaurantId and l.status = :status")
	Optional<Lineup> findByUserIdAndRestaurantIdAndStatus(@Param("userId") Long userId,
		@Param("restaurantId") Long restaurantId, @Param("status") ArrivalStatusEnum statusEnum);

	@Query("select max(l.waitingNumber) from Lineup l where l.restaurant.id = :restaurantId")
	Integer findLastWaitingNumberByRestaurantId(@Param("restaurantId") Long id);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "update Lineup l set l.isDeleted = true where l.restaurant.id = :restaurantId")
	void bulkSoftDeleteByRestaurantId(@Param("restaurantId") Long restaurantId);
}