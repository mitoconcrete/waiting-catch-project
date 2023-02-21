package team.waitingcatch.app.restaurant.dto.restaurant;

import lombok.Getter;

@Getter
public class DeleteRestaurantBySellerServiceRequest {
	private final Long sellerId;

	public DeleteRestaurantBySellerServiceRequest(Long sellerId) {
		this.sellerId = sellerId;
	}
}
