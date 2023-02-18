package team.waitingcatch.app.event.dto.usercoupon;

import lombok.Getter;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.UserCoupon;

@Getter
public class UserCouponResponse {
	private UserCoupon userCoupon;
	private CouponCreator couponCreator;

	public UserCouponResponse(UserCoupon userCoupon, CouponCreator couponCreator) {
		this.userCoupon = userCoupon;
		this.couponCreator = couponCreator;
	}
}
