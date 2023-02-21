package team.waitingcatch.app.restaurant.dto.restaurant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SearchRestaurantServiceRequest {
	private final String keyword;
	private final double latitude;
	private final double longitude;
}
