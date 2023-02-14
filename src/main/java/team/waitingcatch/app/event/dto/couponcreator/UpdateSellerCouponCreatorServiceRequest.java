package team.waitingcatch.app.event.dto.couponcreator;

import java.time.LocalDateTime;

import lombok.Getter;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
public class UpdateSellerCouponCreatorServiceRequest {
	private String name;
	private int discountPrice;
	private CouponTypeEnum discountType;
	private int quantity;
	private LocalDateTime expireDate;
	private Long eventId;
	private Long creatorId;
	private String username;

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
