package team.waitingcatch.app.event.service.couponcreator;

import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateSellerCouponCreatorServiceRequest;

public interface CouponCreatorService {
	//광역 이벤트에서 쿠폰 생성자를 생성한다
	public void createAdminCouponCreator(
		CreateAdminCouponCreatorServiceRequest createAdminCouponCreatorServiceRequest);

	//레스토랑 이벤트에서 쿠폰 생성자를 생성한다
	public void createSellerCouponCreator(
		CreateSellerCouponCreatorServiceRequest createSellerCouponCreatorServiceRequest);

	//광역 쿠폰생성자를 수정한다.
	public void updateAdminCouponCreator(
		UpdateAdminCouponCreatorServiceRequest updateAdminCouponCreatorServiceRequest);

	//레스토랑 쿠폰생성자를 수정한다.
	public void updateSellerCouponCreator(
		UpdateSellerCouponCreatorServiceRequest updateSellerCouponCreatorServiceRequest);
}
