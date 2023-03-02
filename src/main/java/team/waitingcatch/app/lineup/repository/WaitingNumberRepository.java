package team.waitingcatch.app.lineup.repository;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.lineup.entity.WaitingNumber;

public interface WaitingNumberRepository extends JpaRepository<WaitingNumber, Long> {
	@Lock(LockModeType.OPTIMISTIC)
	@Query("select wn from WaitingNumber wn where wn.restaurant.id = :restaurantId")
	Optional<WaitingNumber> findByRestaurantId(@Param("restaurantId") Long restaurantId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("update WaitingNumber w set w.nextNumber = 1")
	void bulkResetWaitingNumber();
}