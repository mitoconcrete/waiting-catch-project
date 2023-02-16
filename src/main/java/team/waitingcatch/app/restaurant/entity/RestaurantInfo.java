package team.waitingcatch.app.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.common.entity.TimeStamped;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantInfo extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "restaurant_info_id")
	private Long id;

	@Column(nullable = false)
	private Long restaurantId;

	@Column(nullable = false)
	private String openTime;

	@Column(nullable = false)
	private String closeTime;

	@Column(nullable = false)
	private float rate = 0;

	@Column(nullable = false)
	private int totalReview = 0;

	@Column(nullable = false)
	private boolean isLineupActiveStatus;

	@Column(nullable = false)
	private int totalLineup;

	// 리뷰 작성시 해당 레스토랑의 평균 별점을 갱신한다.
	public void setAverageRate(float rate) {
		this.rate = rate;
		this.totalReview += 1;
	}
}
