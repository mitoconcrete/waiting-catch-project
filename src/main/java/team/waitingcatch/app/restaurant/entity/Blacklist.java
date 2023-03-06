package team.waitingcatch.app.restaurant.entity;

import static team.waitingcatch.app.exception.ErrorCode.*;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlacklistInternalServiceRequest;
import team.waitingcatch.app.user.entitiy.User;

@Entity
@Table(indexes = {
	@Index(name = "ix_blacklist_restaurant_id", columnList = "restaurant_id"),
	@Index(name = "ix_blacklist_user_id", columnList = "user_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blacklist extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "blacklist_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private boolean isDeleted;

	public Blacklist(CreateBlacklistInternalServiceRequest serviceRequest) {
		this.restaurant = serviceRequest.getRestaurant();
		this.user = serviceRequest.getUser();
	}

	public void deleteSuccess() {
		this.isDeleted = true;
	}

	public void checkDeleteStatus() {
		if (this.isDeleted) {
			throw new DuplicateRequestException(ALREADY_DELETED_BLACKLIST);
		}
	}
}