package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;

@Getter
public class ApproveSignUpSellerServiceRequest {
	private final Long id;

	public ApproveSignUpSellerServiceRequest(Long id) {
		this.id = id;
	}
}
