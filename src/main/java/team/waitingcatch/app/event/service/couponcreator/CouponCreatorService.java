package team.waitingcatch.app.event.service.couponcreator;

import team.waitingcatch.app.event.dto.couponcreator.createCouponCreatorControllerRequest;
import team.waitingcatch.app.event.dto.couponcreator.putCouponCreatorControllerRequest;

public interface CouponCreatorService {
	//특정 이벤트에서 쿠폰 생성자를 생성한다
	public void createCouponCreator(createCouponCreatorControllerRequest createCouponCreatorControllerRequest);

	//쿠폰생성자를 수정한다.
	public void putCouponCreator(putCouponCreatorControllerRequest putCouponCreatorControllerRequest);

}
