package team.waitingcatch.app.lineup.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.lineup.entity.WaitingNumber;

public interface WaitingNumberRepository extends JpaRepository<WaitingNumber, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select wn.waitingNumber from WaitingNumber wn where wn.restaurant.id = :restaurantId")
	int findByRestaurantId(@Param("restaurantId") Long restaurantId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("update WaitingNumber wn set wn.waitingNumber = wn.waitingNumber + 1 where wn.restaurant.id = :restaurantId")
	void updateWaitingNumber(@Param("restaurantId") Long restaurantId);
}