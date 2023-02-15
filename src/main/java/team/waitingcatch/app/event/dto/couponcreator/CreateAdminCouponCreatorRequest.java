package team.waitingcatch.app.event.dto.couponcreator;

import lombok.Getter;
import team.waitingcatch.app.event.entity.Event;

@Getter
public class CreateAdminCouponCreatorRequest {
	private CreateAdminCouponCreatorServiceRequest createAdminCouponCreatorServiceRequest;
	private Event event;

	public CreateAdminCouponCreatorRequest(
		CreateAdminCouponCreatorServiceRequest createAdminCouponCreatorServiceRequest,
		Event event) {
		this.createAdminCouponCreatorServiceRequest = createAdminCouponCreatorServiceRequest;
		this.event = event;
	}
}
