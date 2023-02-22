package team.waitingcatch.app.restaurant.dto.restaurant;

import lombok.Getter;
import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.user.entitiy.User;

@Getter
public class SaveDummyRestaurantRequest {
	private final String name;
	private final Position position;
	private final Address address;
	private final String category;
	private final String phoneNumber;
	private final User user;

	public SaveDummyRestaurantRequest(String name, Address address, Position position, String phone, String category,
		User user) {

		this.name = name;
		this.address = address;
		this.position = position;
		this.phoneNumber = phone;
		this.category = category;
		this.user = user;
	}
}