package team.waitingcatch.app.event.dto.couponcreator;

import java.time.LocalDateTime;

import lombok.Getter;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
public class UpdateCouponCreatorControllerRequest {
	private String name;
	private int discountPrice;
	private CouponTypeEnum discountType;
	private int quantity;
	private LocalDateTime expireDate;
}
