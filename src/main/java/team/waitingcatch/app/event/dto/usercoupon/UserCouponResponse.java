package team.waitingcatch.app.event.dto.usercoupon;

import lombok.Getter;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.UserCoupon;

@Getter
public class UserCouponResponse {
	private final UserCoupon userCoupon;
	private final CouponCreator couponCreator;

	public UserCouponResponse(UserCoupon userCoupon) {
		this.userCoupon = userCoupon;
		this.couponCreator = userCoupon.getCouponCreator();
	}
}
