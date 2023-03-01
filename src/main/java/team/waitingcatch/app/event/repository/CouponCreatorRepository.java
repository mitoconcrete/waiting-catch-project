package team.waitingcatch.app.event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.Event;

public interface CouponCreatorRepository extends JpaRepository<CouponCreator, Long> {
	@Query(value = "select c from CouponCreator c where c.event.id = :eventId and c.isDeleted = false")
	List<CouponCreator> findAllByEventId(@Param("eventId") Long eventId);

	List<CouponCreator> findByEventAndIsDeletedFalse(Event event);

	@Query("select cc from CouponCreator cc join fetch cc.event where cc.event = :event and cc.isDeleted = false")
	List<CouponCreator> findByEventWithEvent(@Param("event") Event event);
}
