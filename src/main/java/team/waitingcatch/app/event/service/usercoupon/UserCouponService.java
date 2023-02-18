package team.waitingcatch.app.event.service.usercoupon;

import java.util.List;

import team.waitingcatch.app.event.dto.usercoupon.CreateUserCouponServiceRequest;
import team.waitingcatch.app.event.dto.usercoupon.GetUserCouponResponse;
import team.waitingcatch.app.user.entitiy.User;

public interface UserCouponService {
	void createUserCoupon(CreateUserCouponServiceRequest createUserCouponserviceRequest);

	List<GetUserCouponResponse> getUserCoupon(User user);
}
