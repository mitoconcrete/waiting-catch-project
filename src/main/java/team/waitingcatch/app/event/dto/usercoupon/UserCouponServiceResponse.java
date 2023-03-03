package team.waitingcatch.app.event.dto.usercoupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.entity.UserCoupon;

@Getter
@RequiredArgsConstructor
public class UserCouponServiceResponse {

	private UserCoupon userCoupons;
	private String restaurants;

	public UserCouponServiceResponse(UserCoupon userCoupons, String restaurants) {
		this.userCoupons = userCoupons;
		this.restaurants = restaurants;
	}
}
