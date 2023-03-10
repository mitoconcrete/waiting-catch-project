package team.waitingcatch.app.event.dto.couponcreator;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
@RequiredArgsConstructor
public class UpdateAdminCouponCreatorServiceRequest {
	@NotNull
	@Size(min = 2, max = 20, message = "쿠폰생성자 이름은 최소 2글자에서 5글자 사이어야합니다.")
	private final String name;

	@NotNull
	private final int discountPrice;

	@NotNull
	private final CouponTypeEnum discountType;

	@NotNull
	private final int quantity;

	@NotNull
	@Future
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private final LocalDateTime expireDate;

	private final Long eventId;

	private final Long creatorId;

	public UpdateAdminCouponCreatorServiceRequest(
		UpdateCouponCreatorControllerRequest updateCouponCreatorControllerRequest,
		Long eventId, Long creatorId) {
		this.name = updateCouponCreatorControllerRequest.getName();
		this.discountPrice = updateCouponCreatorControllerRequest.getDiscountPrice();
		this.discountType = CouponTypeEnum.valueOf(updateCouponCreatorControllerRequest.getDiscountType());
		this.quantity = updateCouponCreatorControllerRequest.getQuantity();
		this.expireDate = updateCouponCreatorControllerRequest.getExpireDate();
		this.eventId = eventId;
		this.creatorId = creatorId;
	}
}