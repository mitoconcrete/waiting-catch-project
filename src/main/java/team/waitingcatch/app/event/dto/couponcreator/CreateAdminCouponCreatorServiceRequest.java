package team.waitingcatch.app.event.dto.couponcreator;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
@RequiredArgsConstructor
public class CreateAdminCouponCreatorServiceRequest {
	private final String name;
	private final int discountPrice;
	private final CouponTypeEnum discountType;
	private final int quantity;
	private final LocalDateTime expireDate;
	private final Long eventId;

	public CreateAdminCouponCreatorServiceRequest(
		CreateCouponCreatorControllerRequest createCouponCreatorControllerRequest,
		Long eventId) {
		this.name = createCouponCreatorControllerRequest.getName();
		this.discountPrice = createCouponCreatorControllerRequest.getDiscountPrice();
		this.discountType = createCouponCreatorControllerRequest.getDiscountType();
		this.quantity = createCouponCreatorControllerRequest.getQuantity();
		this.expireDate = createCouponCreatorControllerRequest.getExpireDate();
		this.eventId = eventId;
	}
}
