package team.waitingcatch.app.event.entity;

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
import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
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
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String eventStartDate;

	@Column(nullable = false)
	private String eventEndDate;

	public Event(CreateEventControllerRequest createEventControllerRequest) {
		this.name = createEventControllerRequest.getName();
		this.eventStartDate = createEventControllerRequest.getEventStartDate();
		this.eventEndDate = createEventControllerRequest.getEventEndDate();
	}

	public Event(CreateEventControllerRequest createEventControllerRequest, Restaurant restaurant) {
		this.name = createEventControllerRequest.getName();
		this.eventStartDate = createEventControllerRequest.getEventStartDate();
		this.eventEndDate = createEventControllerRequest.getEventEndDate();
		this.restaurant = restaurant;
	}
}
