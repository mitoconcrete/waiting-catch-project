package team.waitingcatch.app.event.dto.couponcreator;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
@RequiredArgsConstructor
public class UpdateSellerCouponCreatorServiceRequest {
	private final String name;
	private final int discountPrice;
	private final CouponTypeEnum discountType;
	private final int quantity;
	private final LocalDateTime expireDate;
	private final Long eventId;
	private final Long creatorId;
	private final String username;

	public UpdateSellerCouponCreatorServiceRequest(
		UpdateCouponCreatorControllerRequest updateCouponCreatorControllerRequest,
		Long eventId, Long creatorId, String username) {
		this.name = updateCouponCreatorControllerRequest.getName();
		this.discountPrice = updateCouponCreatorControllerRequest.getDiscountPrice();
		this.discountType = updateCouponCreatorControllerRequest.getDiscountType();
		this.quantity = updateCouponCreatorControllerRequest.getQuantity();
		this.expireDate = updateCouponCreatorControllerRequest.getExpireDate();
		this.eventId = eventId;
		this.creatorId = creatorId;
		this.username = username;
	}
}
