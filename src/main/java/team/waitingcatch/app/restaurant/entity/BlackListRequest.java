package team.waitingcatch.app.restaurant.entity;

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
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;
import team.waitingcatch.app.user.entitiy.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BlackListRequest extends TimeStamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "black_list_request_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private AcceptedStatusEnum status;
	@Column(nullable = false)
	private String description;

	public BlackListRequest(Restaurant restaurant, User user, String description) {
		this.restaurant = restaurant;
		this.user = user;
		this.description = description;
		this.status = AcceptedStatusEnum.WAIT;
	}

	public void updateCancelStatus() {
		this.status = AcceptedStatusEnum.CANCEL;
	}

	public void checkWaitingStatus() {
		if (this.status == AcceptedStatusEnum.APPROVAL) {
			throw new IllegalArgumentException("Already approve the black list request");
		} else if (this.status == AcceptedStatusEnum.CANCEL) {
			throw new IllegalArgumentException("Already cancel the black list request");
		} else if (this.status == AcceptedStatusEnum.REJECTION) {
			throw new IllegalArgumentException("Already reject the black list request");
		}
	}

	public void updateApprovalStatus() {
		this.status = AcceptedStatusEnum.APPROVAL;
	}

	public void checkBlacklistRequest() {
		if (this.status == AcceptedStatusEnum.WAIT) {
			throw new IllegalArgumentException("이미 해당유저의 블랙리스트 요청을 하셨습니다.");
		}
		if (this.status == AcceptedStatusEnum.APPROVAL) {
			throw new IllegalArgumentException("이미 해당유저의 블랙리스트 승인을 하였습니다.");
		}
	}
}
