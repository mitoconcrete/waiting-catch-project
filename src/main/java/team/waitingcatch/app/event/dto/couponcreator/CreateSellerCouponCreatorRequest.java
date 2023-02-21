package team.waitingcatch.app.event.dto.couponcreator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.event.entity.Event;

@Getter
@NoArgsConstructor
public class CreateSellerCouponCreatorRequest {
	private CreateSellerCouponCreatorServiceRequest createSellerCouponCreatorServiceRequest;
	private Event event;

	public CreateSellerCouponCreatorRequest(
		CreateSellerCouponCreatorServiceRequest createSellerCouponCreatorServiceRequest, Event event) {
		this.createSellerCouponCreatorServiceRequest = createSellerCouponCreatorServiceRequest;
		this.event = event;
	}
}
