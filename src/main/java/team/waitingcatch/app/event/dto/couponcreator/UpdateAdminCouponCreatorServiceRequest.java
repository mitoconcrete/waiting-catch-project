package team.waitingcatch.app.event.dto.couponcreator;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
@NoArgsConstructor
public class UpdateAdminCouponCreatorServiceRequest {
	private String name;
	private int discountPrice;
	private CouponTypeEnum discountType;
	private int quantity;
	private LocalDateTime expireDate;
	private Long eventId;
	private Long creatorId;

	public UpdateAdminCouponCreatorServiceRequest(
		UpdateCouponCreatorControllerRequest updateCouponCreatorControllerRequest,
		Long eventId, Long creatorId) {
		this.name = updateCouponCreatorControllerRequest.getName();
		this.discountPrice = updateCouponCreatorControllerRequest.getDiscountPrice();
		this.discountType = updateCouponCreatorControllerRequest.getDiscountType();
		this.quantity = updateCouponCreatorControllerRequest.getQuantity();
		this.expireDate = updateCouponCreatorControllerRequest.getExpireDate();
		this.eventId = eventId;
		this.creatorId = creatorId;
	}
}
