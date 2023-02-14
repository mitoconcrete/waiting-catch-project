package team.waitingcatch.app.event.dto.couponcreator;

import java.time.LocalDateTime;

import lombok.Getter;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
public class CreateSellerCouponCreatorServiceRequest {
	private String name;
	private int discountPrice;
	private CouponTypeEnum discountType;
	private int quantity;
	private LocalDateTime expireDate;
	private Long eventId;
	private String username;

	public CreateSellerCouponCreatorServiceRequest(
		CreateCouponCreatorControllerRequest createCouponCreatorControllerRequest,
		Long eventId, String username) {
		this.name = createCouponCreatorControllerRequest.getName();
		this.discountPrice = createCouponCreatorControllerRequest.getDiscountPrice();
		this.discountType = createCouponCreatorControllerRequest.getDiscountType();
		this.quantity = createCouponCreatorControllerRequest.getQuantity();
		this.expireDate = createCouponCreatorControllerRequest.getExpireDate();
		this.eventId = eventId;
		this.username = username;
	}
}
