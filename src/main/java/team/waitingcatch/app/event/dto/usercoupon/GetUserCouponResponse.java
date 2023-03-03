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

	// public GetUserCouponResponse(UserCouponResponse userCouponResponse, String restaurantName) {
	// 	this.id = userCouponResponse.getUserCoupon().getId();
	// 	this.name = userCouponResponse.getCouponCreator().getName();
	// 	this.restaurantName = restaurantName;
	// 	this.discountPrice = userCouponResponse.getCouponCreator().getDiscountPrice();
	// 	this.discountType = userCouponResponse.getCouponCreator().getDiscountType();
	// 	this.expireDate = userCouponResponse.getCouponCreator().getExpireDate();
	// }

	public GetUserCouponResponse(UserCouponServiceResponse userCouponServiceResponse) {
		this.id = userCouponServiceResponse.getUserCoupons().getId();
		this.name = userCouponServiceResponse.getUserCoupons().getCouponCreator().getName();
		this.restaurantName = userCouponServiceResponse.getRestaurants();
		this.discountPrice = userCouponServiceResponse.getUserCoupons().getCouponCreator().getDiscountPrice();
		this.discountType = userCouponServiceResponse.getUserCoupons().getCouponCreator().getDiscountType();
		this.expireDate = userCouponServiceResponse.getUserCoupons().getCouponCreator().getExpireDate();
	}

}