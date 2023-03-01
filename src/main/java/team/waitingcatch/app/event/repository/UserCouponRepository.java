package team.waitingcatch.app.event.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.event.entity.UserCoupon;
import team.waitingcatch.app.user.entitiy.User;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

	List<UserCoupon> findByUserAndIsUsedFalse(User user);

	Optional<UserCoupon> findByIdAndIsUsedFalse(Long id);

	//@Query("select u from UserCoupon u where u.user = :user AND u.isUsed = false")
	//@Query("select u from UserCoupon u join fetch u.user where u.isUsed = false")
	@Query("select uc from UserCoupon uc join fetch uc.user u join fetch uc.couponCreator where uc.user = :user and uc.isUsed=false")
	List<UserCoupon> findByUserWithUserAndCouponCreator(@Param("user") User user);
}

