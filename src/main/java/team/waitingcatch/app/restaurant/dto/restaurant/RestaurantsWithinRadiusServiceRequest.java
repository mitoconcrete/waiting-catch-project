package team.waitingcatch.app.restaurant.dto.restaurant;

import lombok.Getter;

@Getter
public class RestaurantsWithinRadiusServiceRequest {
	private final double latitude;
	private final double longitude;
	private final int distance;

	public RestaurantsWithinRadiusServiceRequest(double latitude, double longitude, int distance) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.distance = distance;
	}
}
