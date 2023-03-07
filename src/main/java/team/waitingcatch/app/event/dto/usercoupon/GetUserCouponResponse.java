package team.waitingcatch.app.event.dto.usercoupon;

import java.time.LocalDateTime;

import lombok.Getter;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
public class GetUserCouponResponse {
	private final Long id;
	private final String name;
	private final String restaurantName;
	private final int discountPrice;
	private final CouponTypeEnum discountType;
	private final LocalDateTime expireDate;

	public GetUserCouponResponse(UserCouponServiceResponse userCouponServiceResponse) {
		this.id = userCouponServiceResponse.getUserCouponId();
		this.name = userCouponServiceResponse.getCouponCreatorName();
		this.restaurantName = userCouponServiceResponse.getRestaurantName();
		this.discountPrice = userCouponServiceResponse.getDiscountPrice();
		this.discountType = userCouponServiceResponse.getDiscountType();
		this.expireDate = userCouponServiceResponse.getExpireDate();
	}

}