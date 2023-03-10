package team.waitingcatch.app.event.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.event.dto.usercoupon.UserCouponServiceResponse;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.UserCoupon;
import team.waitingcatch.app.user.entitiy.User;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

	Optional<UserCoupon> findByIdAndIsUsedFalse(Long id);

	@Query("select new team.waitingcatch.app.event.dto.usercoupon.UserCouponServiceResponse(uc.id,cc.name,r.name,cc.discountPrice,cc.discountType,cc.expireDate) from UserCoupon uc left join uc.user u left join uc.couponCreator cc left join cc.event e left join e.restaurant r where uc.user = :user and uc.isUsed=false")
	List<UserCouponServiceResponse> findRestaurantNameAndUserAll(@Param("user") User user);

	@Query("select uc from UserCoupon uc left join uc.user left join uc.couponCreator cc left join cc.event e left join e.restaurant r where uc.user = :user and uc.couponCreator = :couponCreator")
	Optional<UserCoupon> findUserCouponWithRelations(@Param("user") User user,
		@Param("couponCreator") CouponCreator couponCreator);

}



