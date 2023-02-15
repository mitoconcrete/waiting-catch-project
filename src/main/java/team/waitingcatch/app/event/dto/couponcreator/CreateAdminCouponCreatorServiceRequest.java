package team.waitingcatch.app.event.dto.couponcreator;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
@NoArgsConstructor
public class CreateAdminCouponCreatorServiceRequest {
	private String name;
	private int discountPrice;
	private CouponTypeEnum discountType;
	private int quantity;
	private LocalDateTime expireDate;
	private Long eventId;

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
