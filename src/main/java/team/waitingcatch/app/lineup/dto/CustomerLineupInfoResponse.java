package team.waitingcatch.app.lineup.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class CustomerLineupInfoResponse {
	private final String name;
	private final String phoneNumber;
	private final String restaurantName;

	@QueryProjection
	public CustomerLineupInfoResponse(String name, String phoneNumber, String restaurantName) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.restaurantName = restaurantName;
	}
}