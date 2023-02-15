package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;

@Getter
public class CreateRequestBlackListEntityPassToBlackListEntityInTheRequestBlackListService {
	@NotNull
	private final Restaurant restaurant;
	@NotNull
	private final User user;

	public CreateRequestBlackListEntityPassToBlackListEntityInTheRequestBlackListService(Restaurant restaurant,
		User user) {
		this.restaurant = restaurant;
		this.user = user;
	}
}
