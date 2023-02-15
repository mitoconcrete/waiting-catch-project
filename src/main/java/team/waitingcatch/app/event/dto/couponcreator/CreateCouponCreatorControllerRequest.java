package team.waitingcatch.app.event.dto.couponcreator;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
@RequiredArgsConstructor
public class CreateCouponCreatorControllerRequest {
	private final String name;
	private final int discountPrice;
	private final CouponTypeEnum discountType;
	private final int quantity;
	private final LocalDateTime expireDate;
}
