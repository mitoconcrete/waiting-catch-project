package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApproveSignUpSellerServiceRequest {
	private Long id;

	public ApproveSignUpSellerServiceRequest(Long id) {
		this.id = id;
	}
}
