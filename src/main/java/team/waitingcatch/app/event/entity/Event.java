package team.waitingcatch.app.event.entity;

import java.time.LocalDateTime;

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
import team.waitingcatch.app.restaurant.entity.Restaurant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends TimeStamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private LocalDateTime eventStartDate;

	@Column(nullable = false)
	private LocalDateTime eventEndDate;

}
