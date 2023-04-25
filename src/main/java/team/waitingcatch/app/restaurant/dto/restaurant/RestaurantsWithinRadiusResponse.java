package team.waitingcatch.app.restaurant.dto.restaurant;

import java.util.List;

import lombok.Getter;

@Getter
public class RestaurantsWithinRadiusResponse {
	private final Long id;
	private final String name;
	private final List<String> imageUrl;
	private final float rate;
	private String[] category;
	private final double distance;
	private final int currentWaitingNumber;
	private final boolean isLineupActive;

	public RestaurantsWithinRadiusResponse(RestaurantsWithinRadiusJpaResponse jpaResponse) {
		this.id = jpaResponse.getId();
		this.name = jpaResponse.getName();
		this.imageUrl = jpaResponse.getImages();
		this.rate = jpaResponse.getRate();
		for (int i = 0; i < jpaResponse.getSearchKeyword().size(); i++) {
			this.category = jpaResponse.getSearchKeyword().get(i).split(" ");
		}
		this.distance = jpaResponse.getDistanceBetween();
		this.currentWaitingNumber = jpaResponse.getCurrentWaitingNumber();
		this.isLineupActive = jpaResponse.isLineupActive();
	}
}