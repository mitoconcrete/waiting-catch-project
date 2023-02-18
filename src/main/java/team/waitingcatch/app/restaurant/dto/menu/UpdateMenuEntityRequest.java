package team.waitingcatch.app.restaurant.dto.menu;

import lombok.Getter;

@Getter
public class UpdateMenuEntityRequest {
	private final String name;
	private final int price;
	private final String imageUrl;

	public UpdateMenuEntityRequest(String name, int price, String imageUrl) {
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
	}
}
