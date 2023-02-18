package team.waitingcatch.app.event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.Event;

public interface CouponCreatorRepository extends JpaRepository<CouponCreator, Long> {

	List<CouponCreator> findByEventAndIsDeletedFalse(Event event);
}
