package team.waitingcatch.app.restaurant.entity;

import static team.waitingcatch.app.exception.ErrorCode.*;

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
import team.waitingcatch.app.exception.DuplicateRequestException;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;
import team.waitingcatch.app.user.entitiy.User;

@Entity
@Table(indexes = {
	@Index(name = "ix_blacklist_demand_restaurant_id", columnList = "restaurant_id"),
	@Index(name = "ix_blacklist_demand_user_id", columnList = "user_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlacklistDemand extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "blacklist_demand_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false, length = 20)
	@Enumerated(value = EnumType.STRING)
	private AcceptedStatusEnum status;

	@Column(nullable = false, length = 200)
	private String description;

	public BlacklistDemand(Restaurant restaurant, User user, String description) {
		this.restaurant = restaurant;
		this.user = user;
		this.description = description;
		this.status = AcceptedStatusEnum.WAIT;
	}

	public void updateCancelStatus() {
		this.status = AcceptedStatusEnum.CANCEL;
	}

	public void updateRejectionStatus() {
		this.status = AcceptedStatusEnum.REJECT;
	}

	public void updateApprovalStatus() {
		this.status = AcceptedStatusEnum.APPROVE;
	}

	public void checkStatus() {
		if (this.status == AcceptedStatusEnum.CANCEL) {
			throw new DuplicateRequestException(ALREADY_CANCELED);
		}
		if (this.status == AcceptedStatusEnum.APPROVE) {
			throw new DuplicateRequestException(ALREADY_APPROVED);
		}
		if (this.status == AcceptedStatusEnum.REJECT) {
			throw new DuplicateRequestException(ALREADY_REJECTED);
		}
	}
}