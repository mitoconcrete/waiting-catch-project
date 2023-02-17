package team.waitingcatch.app.restaurant.dto.restaurant;

import lombok.Getter;

@Getter
public class RestaurantsWithin3kmRadiusServiceRequest {
	private final double latitude;
	private final double longitude;

	public RestaurantsWithin3kmRadiusServiceRequest(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
