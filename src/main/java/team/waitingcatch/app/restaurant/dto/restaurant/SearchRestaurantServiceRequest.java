package team.waitingcatch.app.restaurant.dto.restaurant;

import lombok.Getter;

@Getter
public class SearchRestaurantServiceRequest {
	private final String keyword;
	private final double latitude;
	private final double longitude;

	public SearchRestaurantServiceRequest(String keyword, double latitude, double longitude) {
		this.keyword = keyword;
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
