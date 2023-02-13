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
	private int discount_price;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private CouponTypeEnum discountType;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private LocalDateTime expireDate;
}
