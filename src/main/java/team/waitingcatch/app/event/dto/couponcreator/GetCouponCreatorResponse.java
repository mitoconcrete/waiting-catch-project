package team.waitingcatch.app.event.dto.couponcreator;

import lombok.Getter;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
public class GetCouponCreatorResponse {
	private Long id;
	private String name;
	private int discountPrice;
	private CouponTypeEnum discountType;

	public GetCouponCreatorResponse(CouponCreator couponCreator) {
		this.id = couponCreator.getId();
		this.name = couponCreator.getName();
		this.discountPrice = couponCreator.getDiscountPrice();
		this.discountType = couponCreator.getDiscountType();
	}
}
