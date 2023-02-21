package team.waitingcatch.app.event.dto.usercoupon;

import java.time.LocalDateTime;

import lombok.Getter;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
public class GetUserCouponResponse {
	private Long id;
	private String name;
	private int discountPrice;
	private CouponTypeEnum discountType;
	private LocalDateTime expireDate;

	public GetUserCouponResponse(UserCouponResponse userCouponResponse) {
		this.id = userCouponResponse.getUserCoupon().getId();
		this.name = userCouponResponse.getCouponCreator().getName();
		this.discountPrice = userCouponResponse.getCouponCreator().getDiscountPrice();
		this.discountType = userCouponResponse.getCouponCreator().getDiscountType();
		this.expireDate = userCouponResponse.getCouponCreator().getExpireDate();
	}
}