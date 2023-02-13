package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RejectSignUpSellerServiceRequest {
	private Long id;

	public RejectSignUpSellerServiceRequest(Long id) {
		this.id = id;
	}
}
