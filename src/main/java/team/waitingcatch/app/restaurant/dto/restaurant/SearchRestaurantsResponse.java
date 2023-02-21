package team.waitingcatch.app.restaurant.dto.restaurant;

import lombok.Getter;

@Getter
public class SearchRestaurantsResponse {
	private final String name;
	private final String imageUrl;
	private final float rate;
	private final String[] category;
	private final double distance;
	private final int currentWaitingNumber;
	private final boolean isLineupActive;

	public SearchRestaurantsResponse(SearchRestaurantJpaResponse jpaResponse, double distance) {
		this.name = jpaResponse.getName();
		this.imageUrl = jpaResponse.getImages();
		this.rate = jpaResponse.getRate();
		this.category = jpaResponse.getSearchKeyword().split(" ");
		this.distance = distance;
		this.currentWaitingNumber = jpaResponse.getCurrentWaitingNumber();
		this.isLineupActive = jpaResponse.isLineupActive();
	}
}
