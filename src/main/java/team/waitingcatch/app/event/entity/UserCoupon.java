package team.waitingcatch.app.event.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_coupon_id")
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private CouponCreator couponCreatorId;

	@Column(nullable = false)
	private boolean isUsed;

}
