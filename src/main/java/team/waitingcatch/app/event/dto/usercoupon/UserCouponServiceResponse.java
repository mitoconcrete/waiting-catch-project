package team.waitingcatch.app.event.dto.usercoupon;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
@RequiredArgsConstructor
public class UserCouponServiceResponse {

	private final Long userCouponId;
	private final String couponCreatorName;
	private final String restaurantName;
	private final int discountPrice;
	private final CouponTypeEnum discountType;
	private final LocalDateTime expireDate;
	
}
