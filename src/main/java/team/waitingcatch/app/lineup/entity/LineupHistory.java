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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.common.entity.TimeStamped;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;

@Entity
@Table(indexes = {
	@Index(name = "ix_lineup_history_user_id", columnList = "user_Id"),
	@Index(name = "ix_lineup_history_restaurant_id", columnList = "restaurant_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineupHistory extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lineup_history_id")
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

	@Column(nullable = false)
	private LocalDateTime startedAt;

	private LocalDateTime arrivedAt;

	@Column(nullable = false)
	private boolean isReviewed;

	@Column(nullable = false)
	private boolean isReceivedReviewRequest;

	@Column(nullable = false)
	private boolean isDeleted;

	public static LineupHistory of(Lineup lineup) {
		return new LineupHistory(lineup);
	}

	public LineupHistory(Lineup lineup) {
		this.user = lineup.getUser();
		this.restaurant = lineup.getRestaurant();
		this.waitingNumber = lineup.getWaitingNumber();
		this.numOfMembers = lineup.getNumOfMembers();
		this.status = lineup.getStatus();
		this.callCount = lineup.getCallCount();
		this.startedAt = lineup.getCreatedDate();
		this.arrivedAt = lineup.getArrivedAt();
		this.isReviewed = lineup.isReviewed();
		this.isDeleted = false;
		this.isReceivedReviewRequest = false;
	}

	public void updateIsReviewed() {
		isReviewed = true;
	}
}