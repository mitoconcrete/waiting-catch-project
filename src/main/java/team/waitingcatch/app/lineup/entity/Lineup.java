package team.waitingcatch.app.lineup.entity;

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
import team.waitingcatch.app.lineup.dto.StartLineupEntityRequest;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lineup extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lineup_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@Column(nullable = false)
	private int waitingNumber;

	@Column(nullable = false)
	private int numOfMembers;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ArrivalStatusEnum status;

	@Column(nullable = false)
	private int callCount;

	private LocalDateTime arrivedAt;

	@Column(nullable = false)
	private boolean isReviewed;

	@Column(nullable = false)
	private boolean isDeleted;

	public static Lineup createLineup(StartLineupEntityRequest entityRequest) {
		return new Lineup(entityRequest);
	}

	public ArrivalStatusEnum updateStatus(ArrivalStatusEnum status) {
		if (this.status == ArrivalStatusEnum.CANCEL) {
			throw new IllegalArgumentException("이미 취소된 줄서기입니다.");
		}
		if (this.status == ArrivalStatusEnum.ARRIVE) {
			throw new IllegalArgumentException("이미 완료된 줄서기입니다.");
		}

		if (status == ArrivalStatusEnum.CALL) {
			if (callCount >= 2) {
				throw new IllegalArgumentException("호출은 최대 2번까지 가능합니다.");
			}
			callCount++;
		}
		this.status = status;
		return this.status;
	}

	public void updateIsReviewed() {
		isReviewed = true;
	}

	public boolean isSameUserId(Long userId) {
		return this.getUserId().equals(userId);
	}

	public boolean isSameRestaurant(Restaurant restaurant) {
		return this.restaurant.getId().equals(restaurant.getId());
	}

	public Long getUserId() {
		return user.getId();
	}

	private Lineup(StartLineupEntityRequest entityRequest) {
		this.user = entityRequest.getUser();
		this.restaurant = entityRequest.getRestaurant();
		this.waitingNumber = entityRequest.getWaitingNumber();
		this.numOfMembers = entityRequest.getNumOfMembers();
		this.status = ArrivalStatusEnum.WAIT;
		this.arrivedAt = null;
		this.isReviewed = false;
		this.isDeleted = false;
		this.callCount = 0;
	}
}