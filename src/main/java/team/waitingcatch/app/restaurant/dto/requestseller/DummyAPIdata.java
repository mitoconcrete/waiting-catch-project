package team.waitingcatch.app.restaurant.dto.requestseller;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import team.waitingcatch.app.common.Position;

@Getter
public class DummyAPIdata {
	private final String username; //Id
	private final String name; //id랑 똑같이
	private final String email;//id+gmail.cam
	private final String phoneNumber;
	private final String restaurantName;//placename
	private final List<String> categories; //categoryList
	private final String description; // test
	private final Position position;// position
	private final String zipCode; // 임의값00000
	private final String address;//address
	private final String detailAddress; // test
	// private final Address address;
	// private final List<String> searchKeyWords;
	private final String businessLicenseNo; //uuid

	private final String nullPhoneNumber;

	public DummyAPIdata(String username, String name, String email, String phoneNumber, String restaurantName,
		List<String> categories, Position position, String address, String nullPhoneNumber) {
		this.username = username;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.restaurantName = restaurantName;
		this.categories = categories;
		this.description = "test dummy";
		this.position = position;
		this.zipCode = "00000";
		this.address = address;
		this.detailAddress = "test detail address";
		this.businessLicenseNo = String.valueOf(UUID.randomUUID()).substring(0, 10);
		this.nullPhoneNumber = nullPhoneNumber;
	}

}
