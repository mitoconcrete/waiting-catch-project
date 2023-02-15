package team.waitingcatch.app.event.dto.couponcreator;

import lombok.Getter;
import team.waitingcatch.app.event.entity.Event;

@Getter
public class CreateSellerCouponCreatorRequest {
	private CreateSellerCouponCreatorServiceRequest createSellerCouponCreatorServiceRequest
	private Event event;

	public CreateSellerCouponCreatorRequest(
		CreateSellerCouponCreatorServiceRequest createSellerCouponCreatorServiceRequest, Event event) {
		this.createSellerCouponCreatorServiceRequest = createSellerCouponCreatorServiceRequest;
		this.event = event;
	}
}
