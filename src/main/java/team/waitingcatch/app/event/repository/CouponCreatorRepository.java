package team.waitingcatch.app.event.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.Event;

public interface CouponCreatorRepository extends JpaRepository<CouponCreator, Long> {
	@Query(value = "select c from CouponCreator c where c.event.id = :eventId and c.isDeleted = false")
	List<CouponCreator> findAllByEventId(@Param("eventId") Long eventId);

	@Query("select cc from CouponCreator cc where cc.event.id in :events")
	List<CouponCreator> findByEvent(@Param("events") List<Event> event);

	@Lock(LockModeType.OPTIMISTIC)
	@Query("select cc from CouponCreator cc where cc.id = :id")
	CouponCreator getHasCouponBalance(@Param("id") Long id);

}
