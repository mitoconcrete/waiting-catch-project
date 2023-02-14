package team.waitingcatch.app.event.service.couponcreator;

import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateCouponCreatorControllerRequest;

public interface CouponCreatorService {
	//광역 이벤트에서 쿠폰 생성자를 생성한다
	public String createAdminCouponCreator(
		CreateAdminCouponCreatorServiceRequest createAdminCouponCreatorServiceRequest);

	//레스토랑 이벤트에서 쿠폰 생성자를 생성한다
	public String createSellerCouponCreator(
		CreateSellerCouponCreatorServiceRequest createSellerCouponCreatorServiceRequest);

	//쿠폰생성자를 수정한다.
	public String updateCouponCreator(UpdateCouponCreatorControllerRequest putCouponCreatorControllerRequest);

}
