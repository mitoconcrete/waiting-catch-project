package team.waitingcatch.app.restaurant.dto.restaurant;

import java.util.List;

import lombok.Getter;

@Getter
public class RestaurantsWithinRadiusResponse {
	private final Long id;
	private final String name;
	private final List<String> imageUrl;
	private final float rate;
	private final String[] category;
	private final double distance;
	private final int currentWaitingNumber;
	private final boolean isLineupActive;

	public RestaurantsWithinRadiusResponse(RestaurantsWithinRadiusJpaResponse jpaResponse, double distance) {
		this.id = jpaResponse.getId();
		this.name = jpaResponse.getName();
		this.imageUrl = jpaResponse.getImages();
		this.rate = jpaResponse.getRate();
		this.category = jpaResponse.getSearchKeyword().split(" ");
		this.distance = distance;
		this.currentWaitingNumber = jpaResponse.getCurrentWaitingNumber();
		this.isLineupActive = jpaResponse.isLineupActive();
	}
}
