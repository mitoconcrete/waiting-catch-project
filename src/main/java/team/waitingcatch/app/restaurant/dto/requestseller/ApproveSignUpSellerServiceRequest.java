package team.waitingcatch.app.restaurant.dto.requestseller;

import lombok.Getter;

@Getter
public class ApproveSignUpSellerServiceRequest {
	private final Long id;

	public ApproveSignUpSellerServiceRequest(Long id) {
		this.id = id;
	}
}
