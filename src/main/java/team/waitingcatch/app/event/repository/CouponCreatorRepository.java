package team.waitingcatch.app.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.event.entity.CouponCreator;

public interface CouponCreatorRepository extends JpaRepository<CouponCreator, Long> {
}
