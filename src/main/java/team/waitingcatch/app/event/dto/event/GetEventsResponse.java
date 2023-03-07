package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import team.waitingcatch.app.event.dto.couponcreator.GetCouponCreatorResponse;
import team.waitingcatch.app.event.entity.Event;

@Getter
public class GetEventsResponse {
	private Long eventId;
	private String name;
	private LocalDateTime eventStartDate;
	private LocalDateTime eventEndDate;
	private List<GetCouponCreatorResponse> couponCreators;

	public GetEventsResponse(Event event, List<GetCouponCreatorResponse> getCouponCreatorResponse) {
		this.eventId = event.getId();
		this.name = event.getName();
		this.eventStartDate = event.getEventStartDate();
		this.eventEndDate = event.getEventEndDate();
		this.couponCreators = getCouponCreatorResponse;
	}
}