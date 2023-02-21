package team.waitingcatch.app.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlacklistInternalServiceRequest;
import team.waitingcatch.app.user.entitiy.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BlackList extends TimeStamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "black_list_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted;

	public BlackList(
		CreateBlacklistInternalServiceRequest createBlackListInternalServiceRequest) {
		this.restaurant = createBlackListInternalServiceRequest.getRestaurant();
		this.user = createBlackListInternalServiceRequest.getUser();
	}

	public boolean isSameRequester(Long sellerId) {
		return restaurant.getUser().getId().equals(sellerId);
	}

	public void deleteSuccess() {
		this.isDeleted = true;
	}

	public void checkDeleteStatus() {
		if (this.isDeleted) {
			throw new IllegalArgumentException("blacklist user is already deleted");
		}
	}
}