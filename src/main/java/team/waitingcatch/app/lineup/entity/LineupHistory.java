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
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineupHistory {
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
}