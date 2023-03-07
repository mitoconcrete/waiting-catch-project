package team.waitingcatch.app.event.dto.usercoupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateUserCouponServiceRequest {
	private final Long creatorId;
	private final String username;

}
