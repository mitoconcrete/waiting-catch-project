package team.waitingcatch.app.lineup.entity;

import static team.waitingcatch.app.exception.ErrorCode.*;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.common.entity.TimeStamped;
import team.waitingcatch.app.exception.DuplicateRequestException;
import team.waitingcatch.app.lineup.dto.StartLineupEntityRequest;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;

@Entity
@Table(indexes = {
	@Index(name = "ix_lineup_user_id", columnList = "user_id"),
	@Index(name = "ix_lineup_restaurant_id", columnList = "restaurant_id")
})
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
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
	@Column(nullable = false, length = 20)
	private ArrivalStatusEnum status;

	@Column(nullable = false)
	private int callCount;

	private LocalDateTime arrivedAt;

	@Column(nullable = false)
	private boolean isReviewed;

	@Column(nullable = false)
	private boolean isReceivedReviewRequest;

	@Column(nullable = false)
	private boolean isDeleted;

	@Transient
	public final static int MAX_CALL_COUNT = 2;

	public static Lineup of(StartLineupEntityRequest entityRequest) {
		return new Lineup(entityRequest);
	}

	public ArrivalStatusEnum updateStatus(ArrivalStatusEnum status) {
		if (this.status == ArrivalStatusEnum.CANCEL) {
			throw new DuplicateRequestException(LINEUP_ALEADY_CANCELED);
		}
		if (this.status == ArrivalStatusEnum.ARRIVE) {
			throw new DuplicateRequestException(LINEUP_ALEADY_ARRIVED);
		}

		if (status == ArrivalStatusEnum.CALL) {
			if (this.callCount >= MAX_CALL_COUNT) {
				throw new IllegalArgumentException(EXCEED_MAX_CALL_COUNT.getMessage());
			}
			this.callCount++;
		} else if (status == ArrivalStatusEnum.ARRIVE) {
			this.arrivedAt = LocalDateTime.now();
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

	public Long getRestaurantId() {
		return restaurant.getId();
	}

	private Lineup(StartLineupEntityRequest entityRequest) {
		this.user = entityRequest.getUser();
		this.restaurant = entityRequest.getRestaurant();
		this.waitingNumber = entityRequest.getWaitingNumber();
		this.numOfMembers = entityRequest.getNumOfMembers();
		this.status = ArrivalStatusEnum.WAIT;
		this.callCount = 0;
		this.arrivedAt = null;
		this.isReviewed = false;
		this.isReceivedReviewRequest = false;
		this.isDeleted = false;
	}
}