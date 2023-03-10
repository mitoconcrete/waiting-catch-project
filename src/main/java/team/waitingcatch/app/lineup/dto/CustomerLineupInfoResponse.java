package team.waitingcatch.app.lineup.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class CustomerLineupInfoResponse {
	private final long lineupId;
	private final String name;
	private final String phoneNumber;
	private final long restaurantId;
	private final String restaurantName;

	@QueryProjection
	public CustomerLineupInfoResponse(long lineupId, String name, String phoneNumber, long restaurantId,
		String restaurantName) {

		this.lineupId = lineupId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.restaurantId = restaurantId;
		this.restaurantName = restaurantName;
	}
}