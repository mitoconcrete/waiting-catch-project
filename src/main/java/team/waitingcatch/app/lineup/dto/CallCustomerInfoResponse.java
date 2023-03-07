package team.waitingcatch.app.lineup.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class CallCustomerInfoResponse {
	private final String phoneNumber;
	private final String restaurantName;
	private final int waitingNumber;

	@QueryProjection
	public CallCustomerInfoResponse(String phoneNumber, String restaurantName, int waitingNumber) {
		this.phoneNumber = phoneNumber;
		this.restaurantName = restaurantName;
		this.waitingNumber = waitingNumber;
	}
}