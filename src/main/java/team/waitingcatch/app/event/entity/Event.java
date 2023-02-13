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
import team.waitingcatch.app.event.dto.event.CreateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.UpdateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.UpdateSellerEventServiceRequest;
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

	public Event(CreateEventServiceRequest createEventServiceRequest, Restaurant restaurant) {
		this.name = createEventServiceRequest.getName();
		this.eventStartDate = createEventServiceRequest.getEventStartDate();
		this.eventEndDate = createEventServiceRequest.getEventEndDate();
		this.restaurant = restaurant;
	}

	public void updateAdminEvent(UpdateEventServiceRequest updateEventServiceRequest) {
		this.name = updateEventServiceRequest.getName();
		this.eventStartDate = updateEventServiceRequest.getEventStartDate();
		this.eventEndDate = updateEventServiceRequest.getEventEndDate();
	}

	public void updateSellerEvent(UpdateSellerEventServiceRequest updateSellerEventServiceRequest) {
		this.name = updateSellerEventServiceRequest.getName();
		this.eventStartDate = updateSellerEventServiceRequest.getEventStartDate();
		this.eventEndDate = updateSellerEventServiceRequest.getEventEndDate();
	}
}
