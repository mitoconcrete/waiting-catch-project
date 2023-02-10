package team.waitingcatch.app.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.waitingcatch.app.event.entity.UserCoupon;
@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon,Long> {
}
