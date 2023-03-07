package team.waitingcatch.app.restaurant.dto.menu;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Restaurant;

@Getter
public class CreateMenuEntityRequest {
	private final Restaurant restaurant;
	private final String name;
	private final int price;
	private final String image;

	public CreateMenuEntityRequest(Restaurant restaurant, String name, int price, String image) {
		this.restaurant = restaurant;
		this.name = name;
		this.price = price;
		this.image = image;
	}
}
