package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;

import lombok.Getter;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
public class GetCouponCreatorResponse {
	private final Long couponCreatorId;
	private final String name;
	private final int discountPrice;
	private final CouponTypeEnum discountType;
	private final int quantity;
	private final LocalDateTime expireDate;

	public GetCouponCreatorResponse(CouponCreator couponCreator) {
		this.couponCreatorId = couponCreator.getId();
		this.name = couponCreator.getName();
		this.discountPrice = couponCreator.getDiscountPrice();
		this.discountType = couponCreator.getDiscountType();
		this.quantity = couponCreator.getQuantity();
		this.expireDate = couponCreator.getExpireDate();
	}
}
