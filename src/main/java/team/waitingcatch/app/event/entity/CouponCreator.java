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
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.common.entity.TimeStamped;
import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateSellerCouponCreatorRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Entity
@Table
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponCreator extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_creator_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false)
	private int discountPrice;

	@Column(nullable = false, length = 20)
	@Enumerated(value = EnumType.STRING)
	private CouponTypeEnum discountType;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private LocalDateTime expireDate;

	@Column(nullable = false)
	private boolean isDeleted;

	@Version
	@Column(nullable = false)
	private Long version;

	public CouponCreator(CreateAdminCouponCreatorRequest request) {
		this.event = request.getEvent();
		this.name = request.getCreateAdminCouponCreatorServiceRequest().getName();
		this.discountPrice = request.getCreateAdminCouponCreatorServiceRequest().getDiscountPrice();
		this.discountType = request.getCreateAdminCouponCreatorServiceRequest().getDiscountType();
		this.quantity = request.getCreateAdminCouponCreatorServiceRequest().getQuantity();
		this.expireDate = request.getCreateAdminCouponCreatorServiceRequest().getExpireDate();
	}

	public CouponCreator(CreateAdminCouponCreatorRequest request, Long id) {
		this.id = id;
		this.event = request.getEvent();
		this.name = request.getCreateAdminCouponCreatorServiceRequest().getName();
		this.discountPrice = request.getCreateAdminCouponCreatorServiceRequest().getDiscountPrice();
		this.discountType = request.getCreateAdminCouponCreatorServiceRequest().getDiscountType();
		this.quantity = request.getCreateAdminCouponCreatorServiceRequest().getQuantity();
		this.expireDate = request.getCreateAdminCouponCreatorServiceRequest().getExpireDate();
	}

	public CouponCreator(CreateSellerCouponCreatorRequest request) {
		this.event = request.getEvent();
		this.name = request.getCreateSellerCouponCreatorServiceRequest().getName();
		this.discountPrice = request.getCreateSellerCouponCreatorServiceRequest().getDiscountPrice();
		this.discountType = request.getCreateSellerCouponCreatorServiceRequest().getDiscountType();
		this.quantity = request.getCreateSellerCouponCreatorServiceRequest().getQuantity();
		this.expireDate = request.getCreateSellerCouponCreatorServiceRequest().getExpireDate();
	}

	public void updateAdminCouponCreator(UpdateAdminCouponCreatorServiceRequest serviceRequest) {
		this.name = serviceRequest.getName();
		this.discountPrice = serviceRequest.getDiscountPrice();
		this.discountType = serviceRequest.getDiscountType();
		this.quantity = serviceRequest.getQuantity();
		this.expireDate = serviceRequest.getExpireDate();
	}

	public void updateSellerCouponCreator(UpdateSellerCouponCreatorServiceRequest serviceRequest) {
		this.name = serviceRequest.getName();
		this.discountPrice = serviceRequest.getDiscountPrice();
		this.discountType = serviceRequest.getDiscountType();
		this.quantity = serviceRequest.getQuantity();
		this.expireDate = serviceRequest.getExpireDate();
	}

	public void useCoupon() {
		this.quantity -= 1;
	}

}