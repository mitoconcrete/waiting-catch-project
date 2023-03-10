package team.waitingcatch.app.restaurant.dto.restaurant;

import lombok.Getter;

@Getter
public class GetRestaurantInfo {

	private final String phoneNumber;
	private final int capacity;
	private final String description;
	private final String openTime;
	private final String closeTime;

	public GetRestaurantInfo(String phoneNumber, int capacity, String description, String openTime,
		String closeTime) {
		this.phoneNumber = phoneNumber;
		this.capacity = capacity;
		this.description = description;
		this.openTime = openTime;
		this.closeTime = closeTime;
	}
}
