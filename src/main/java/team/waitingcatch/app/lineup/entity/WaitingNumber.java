package team.waitingcatch.app.lineup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.restaurant.entity.Restaurant;

@Entity
@Table(indexes = @Index(columnList = "restaurant_id", unique = true))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingNumber {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "waiting_number_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", nullable = false, unique = true)
	private Restaurant restaurant;

	@Column(nullable = false)
	private int waitingNumber;

	public static WaitingNumber createWaitingNumber(Restaurant restaurant) {
		return new WaitingNumber(restaurant);
	}

	private WaitingNumber(Restaurant restaurant) {
		this.restaurant = restaurant;
		this.waitingNumber = 1;
	}
}