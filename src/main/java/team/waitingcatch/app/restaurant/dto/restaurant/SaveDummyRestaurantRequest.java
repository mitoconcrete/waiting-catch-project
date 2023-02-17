package team.waitingcatch.app.restaurant.dto.restaurant;

import javax.persistence.Column;

import lombok.Getter;
import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.user.entitiy.User;

@Getter
public class SaveDummyRestaurantRequest {

	@Column(nullable = false)
	private String name;
	private Position position;
	private Address address;
	private String category;

	private String phoneNumber;

	private User user;

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
