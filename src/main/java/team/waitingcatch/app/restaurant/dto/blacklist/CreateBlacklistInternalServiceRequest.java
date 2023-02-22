package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;

@Getter
public class CreateBlacklistInternalServiceRequest {
	private final Restaurant restaurant;
	private final User user;

	public CreateBlacklistInternalServiceRequest(Restaurant restaurant, User user) {
		this.restaurant = restaurant;
		this.user = user;
	}
}