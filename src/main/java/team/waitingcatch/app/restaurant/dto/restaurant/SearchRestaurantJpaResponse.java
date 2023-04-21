package team.waitingcatch.app.restaurant.dto.restaurant;

import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import team.waitingcatch.app.common.Position;

@Getter
public class SearchRestaurantJpaResponse {
	private final Long id;
	private final String name;
	private final List<String> images;
	private final float rate;
	private final List<String> searchKeyword;
	private final double latitude;
	private final double longitude;
	private final int currentWaitingNumber;
	private final boolean isLineupActive;

	@QueryProjection
	public SearchRestaurantJpaResponse(Long id, String name, List<String> images, float rate,
		List<String> searchKeyword, Position position, int currentWaitingNumber, boolean isLineupActive) {

		this.id = id;
		this.name = name;
		this.images = images;
		this.rate = rate;
		this.searchKeyword = searchKeyword;
		this.latitude = position.getLatitude();
		this.longitude = position.getLongitude();
		this.currentWaitingNumber = currentWaitingNumber;
		this.isLineupActive = isLineupActive;
	}
}