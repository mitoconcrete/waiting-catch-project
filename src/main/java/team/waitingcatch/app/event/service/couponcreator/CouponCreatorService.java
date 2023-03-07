package team.waitingcatch.app.event.service.couponcreator;

import java.util.List;

import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.event.GetCouponCreatorResponse;

public interface CouponCreatorService {
	//광역 이벤트에서 쿠폰 생성자를 생성한다
	void createAdminCouponCreator(
		CreateAdminCouponCreatorServiceRequest createAdminCouponCreatorServiceRequest);

	// 광역 이벤트 쿠폰 생성자를 조회한다.
	List<GetCouponCreatorResponse> getAdminCouponCreator(Long eventId);

	//레스토랑 이벤트에서 쿠폰 생성자를 생성한다
	void createSellerCouponCreator(
		CreateSellerCouponCreatorServiceRequest createSellerCouponCreatorServiceRequest);

	//광역 쿠폰생성자를 수정한다.
	void updateAdminCouponCreator(
		UpdateAdminCouponCreatorServiceRequest updateAdminCouponCreatorServiceRequest);

	//레스토랑 쿠폰생성자를 수정한다.
	void updateSellerCouponCreator(
		UpdateSellerCouponCreatorServiceRequest updateSellerCouponCreatorServiceRequest);

}
