package team.waitingcatch.app.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.event.entity.UserCoupon;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
}
