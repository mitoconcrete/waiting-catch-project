package team.waitingcatch.app.event.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.common.entity.TimeStamped;
import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateSellerCouponCreatorRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponCreator extends TimeStamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_creator_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private int discountPrice;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private CouponTypeEnum discountType;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private LocalDateTime expireDate;

	public CouponCreator(CreateAdminCouponCreatorRequest createAdminCouponCreatorRequest) {
		this.event = createAdminCouponCreatorRequest.getEvent();
		this.name = createAdminCouponCreatorRequest.getCreateAdminCouponCreatorServiceRequest().getName();
		this.discountPrice = createAdminCouponCreatorRequest.getCreateAdminCouponCreatorServiceRequest()
			.getDiscountPrice();
		this.discountType = createAdminCouponCreatorRequest.getCreateAdminCouponCreatorServiceRequest()
			.getDiscountType();
		this.quantity = createAdminCouponCreatorRequest.getCreateAdminCouponCreatorServiceRequest().getQuantity();
		this.expireDate = createAdminCouponCreatorRequest.getCreateAdminCouponCreatorServiceRequest().getExpireDate();
	}

	public CouponCreator(CreateSellerCouponCreatorRequest createAdminCouponCreatorRequest) {
		this.event = createAdminCouponCreatorRequest.getEvent();
		this.name = createAdminCouponCreatorRequest.getCreateSellerCouponCreatorServiceRequest().getName();
		this.discountPrice = createAdminCouponCreatorRequest.getCreateSellerCouponCreatorServiceRequest()
			.getDiscountPrice();
		this.discountType = createAdminCouponCreatorRequest.getCreateSellerCouponCreatorServiceRequest()
			.getDiscountType();
		this.quantity = createAdminCouponCreatorRequest.getCreateSellerCouponCreatorServiceRequest().getQuantity();
		this.expireDate = createAdminCouponCreatorRequest.getCreateSellerCouponCreatorServiceRequest().getExpireDate();
	}

	public void updateAdminCouponCreator(
		UpdateAdminCouponCreatorServiceRequest updateAdminCouponCreatorServiceRequest) {
		this.name = updateAdminCouponCreatorServiceRequest.getName();
		this.discountPrice = updateAdminCouponCreatorServiceRequest.getDiscountPrice();
		this.discountType = updateAdminCouponCreatorServiceRequest.getDiscountType();
		this.quantity = updateAdminCouponCreatorServiceRequest.getQuantity();
		this.expireDate = updateAdminCouponCreatorServiceRequest.getExpireDate();
	}

	public void updateSellerCouponCreator(
		UpdateSellerCouponCreatorServiceRequest updateSellerCouponCreatorServiceRequest) {
		this.name = updateSellerCouponCreatorServiceRequest.getName();
		this.discountPrice = updateSellerCouponCreatorServiceRequest.getDiscountPrice();
		this.discountType = updateSellerCouponCreatorServiceRequest.getDiscountType();
		this.quantity = updateSellerCouponCreatorServiceRequest.getQuantity();
		this.expireDate = updateSellerCouponCreatorServiceRequest.getExpireDate();
	}
}
