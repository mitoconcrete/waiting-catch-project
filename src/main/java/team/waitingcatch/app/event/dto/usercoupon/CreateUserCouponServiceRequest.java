package team.waitingcatch.app.event.dto.usercoupon;

import lombok.Getter;

@Getter
public class CreateUserCouponServiceRequest {
	private Long creatorId;
	private String username;

	public CreateUserCouponServiceRequest(Long creatorId, String username) {
		this.creatorId = creatorId;
		this.username = username;
	}
}
