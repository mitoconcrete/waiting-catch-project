package team.waitingcatch.app.restaurant.dto.requestseller;

import lombok.Getter;

@Getter
public class RejectSignUpSellerServiceRequest {
	private final Long id;

	public RejectSignUpSellerServiceRequest(Long id) {
		this.id = id;
	}
}
