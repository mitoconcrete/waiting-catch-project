package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;

@Getter
public class CreateRequestBlackListEntityPassToBlackListEntityInTheBlackListService {
	@NotNull
	private final Restaurant restaurant;
	@NotNull
	private final User user;

	public CreateRequestBlackListEntityPassToBlackListEntityInTheBlackListService(
		CreateRequestBlackListEntityPassToBlackListEntityInTheRequestBlackListService createRequestBlackListEntityPassToBlackListEntityInTheRequestBlackListService) {
		this.restaurant = createRequestBlackListEntityPassToBlackListEntityInTheRequestBlackListService.getRestaurant();
		this.user = createRequestBlackListEntityPassToBlackListEntityInTheRequestBlackListService.getUser();
	}
}
