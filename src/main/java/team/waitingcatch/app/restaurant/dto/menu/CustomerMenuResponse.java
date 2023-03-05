package team.waitingcatch.app.restaurant.dto.menu;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Menu;

@Getter
public class CustomerMenuResponse {
	private final String name;
	private final int price;
	private final String imageUrl;

	public CustomerMenuResponse(Menu menu) {
		this.name = menu.getName();
		this.price = menu.getPrice();
		this.imageUrl = menu.getImagePaths();
	}
}