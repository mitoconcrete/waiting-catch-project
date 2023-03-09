package team.waitingcatch.app.restaurant.dto.restaurant;

import org.springframework.data.domain.Pageable;

import lombok.Getter;

@Getter
public class RestaurantsWithinRadiusServiceRequest {
	// private final Long id;
	private final Double lastDistance;
	private final double latitude;
	private final double longitude;
	private final int distance;
	private final Pageable pageable;

	public RestaurantsWithinRadiusServiceRequest(Double lastDistance, double latitude, double longitude, int distance,
		Pageable pageable) {
		// this.id = id;
		this.lastDistance = lastDistance;
		this.latitude = latitude;
		this.longitude = longitude;
		this.distance = distance;
		this.pageable = pageable;
	}
}
