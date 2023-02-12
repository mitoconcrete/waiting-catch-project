package team.waitingcatch.app.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
	private boolean isLineupActiveStatus;

	@Column(nullable = false)
	private int accumulateCount;
}
