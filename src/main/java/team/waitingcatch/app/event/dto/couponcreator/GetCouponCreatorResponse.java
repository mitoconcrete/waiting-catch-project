package team.waitingcatch.app.event.dto.couponcreator;

import java.time.LocalDateTime;

import lombok.Getter;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
public class GetCouponCreatorResponse {
	private Long id;
	private String name;
	private int discountPrice;
	private CouponTypeEnum discountType;
	private LocalDateTime expireDate;

	public GetCouponCreatorResponse(Long id, String name, int discountPrice,
		CouponTypeEnum discountType, LocalDateTime expireDate) {
		this.id = id;
		this.name = name;
		this.discountPrice = discountPrice;
		this.discountType = discountType;
		this.expireDate = expireDate;
	}

	public GetCouponCreatorResponse(CouponCreator couponCreator) {
		this.id = couponCreator.getId();
		this.name = couponCreator.getName();
		this.discountPrice = couponCreator.getDiscountPrice();
		this.discountType = couponCreator.getDiscountType();
		this.expireDate = couponCreator.getExpireDate();
	}
}
