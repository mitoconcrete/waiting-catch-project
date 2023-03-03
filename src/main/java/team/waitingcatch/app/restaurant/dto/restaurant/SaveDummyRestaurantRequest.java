package team.waitingcatch.app.restaurant.dto.restaurant;

import java.util.List;

import lombok.Getter;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.user.entitiy.User;

@Getter
public class SaveDummyRestaurantRequest {
	private final String name;
	private final Position position;
	private final String zipCode;
	private final String address;
	private final String detailAddress;
	private final String category;
	private final String phoneNumber;
	private final User user;
	private final List<String> searchkeywords;

	public SaveDummyRestaurantRequest(String name, String zipCode, String address, String detailAddress,
		Position position, String phone, String category,
		User user, List<String> searchkeywords) {

		this.name = name;
		this.zipCode = zipCode;
		this.address = address;
		this.detailAddress = detailAddress;
		this.position = position;
		this.phoneNumber = phone;
		this.category = category;
		this.user = user;
		this.searchkeywords = searchkeywords;
	}
}