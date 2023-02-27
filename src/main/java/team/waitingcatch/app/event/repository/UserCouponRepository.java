package team.waitingcatch.app.event.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.event.entity.UserCoupon;
import team.waitingcatch.app.user.entitiy.User;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

	List<UserCoupon> findByUserAndIsUsedFalse(User user);

	Optional<UserCoupon> findByIdAndIsUsedFalse(Long id);
}
