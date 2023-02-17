package team.waitingcatch.app.restaurant.dto.menu;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Menu;

@Getter
public class MenuResponse {
	private final Long menuId;
	private final String name;
	private final int price;
	private final String imageUrl;

	public MenuResponse(Menu menu) {
		this.menuId = menu.getId();
		this.name = menu.getName();
		this.price = menu.getPrice();
		this.imageUrl = menu.getImages();
	}
}
