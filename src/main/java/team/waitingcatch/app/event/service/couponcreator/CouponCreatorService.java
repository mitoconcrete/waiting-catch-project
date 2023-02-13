package team.waitingcatch.app.event.service.couponcreator;

import team.waitingcatch.app.event.dto.couponcreator.CreateCouponCreatorControllerRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateCouponCreatorControllerRequest;

public interface CouponCreatorService {
	//특정 이벤트에서 쿠폰 생성자를 생성한다
	public String createCouponCreator(CreateCouponCreatorControllerRequest createCouponCreatorControllerRequest);

	//쿠폰생성자를 수정한다.
	public String updateCouponCreator(UpdateCouponCreatorControllerRequest putCouponCreatorControllerRequest);

}
