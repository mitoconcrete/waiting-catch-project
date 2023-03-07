package team.waitingcatch.app.event.dto.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import team.waitingcatch.app.event.dto.couponcreator.GetCouponCreatorResponse;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.Event;

@Getter
public class GetEventsResponse {
	private Long eventId;
	private String name;
	private LocalDateTime eventStartDate;
	private LocalDateTime eventEndDate;
	private List<GetCouponCreatorResponse> couponCreators;

	public GetEventsResponse(Event event, List<CouponCreator> couponCreators) {
		this.eventId = event.getId();
		this.name = event.getName();
		this.eventStartDate = event.getEventStartDate();
		this.eventEndDate = event.getEventEndDate();
		this.couponCreators = couponCreators.stream().map(GetCouponCreatorResponse::new).collect(Collectors.toList());
	}

	public GetEventsResponse(Long eventId, String name, LocalDateTime eventStartDate, LocalDateTime eventEndDate) {
		this.eventId = eventId;
		this.name = name;
		this.eventStartDate = eventStartDate;
		this.eventEndDate = eventEndDate;
	}

	public void GetEventsSettingResponse(List<GetCouponCreatorResponse> couponCreators) {
		this.couponCreators = couponCreators;
	}
}
