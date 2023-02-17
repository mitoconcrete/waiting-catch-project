package team.waitingcatch.app.restaurant.dto.restaurant;

import lombok.Getter;
import team.waitingcatch.app.common.Position;

@Getter
public class SearchRestaurantJpaResponse {
	private final String name;
	private final String images;
	private final float rate;
	private final String searchKeyword;
	private final double latitude;
	private final double longitude;
	private final int currentWaitingNumber;

	public SearchRestaurantJpaResponse(String name, String images, float rate, String searchKeyword,
		Position position, int currentWaitingNumber) {
		this.name = name;
		this.images = images;
		this.rate = rate;
		this.searchKeyword = searchKeyword;
		this.latitude = position.getLatitude();
		this.longitude = position.getLongitude();
		this.currentWaitingNumber = currentWaitingNumber;

	}
}
