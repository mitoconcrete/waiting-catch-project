package team.waitingcatch.app.restaurant.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.common.entity.TimeStamped;
import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.SaveDummyRestaurantRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantEntityRequest;
import team.waitingcatch.app.user.entitiy.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "restaurant_id")
	private Long id;

	@Column(nullable = false, length = 8)
	private String name;

	private String images;

	@Embedded
	private Position position;

	@Column(nullable = false, length = 5)
	private String zipCode;

	@Column(nullable = false, length = 50)
	private String address;

	@Column(nullable = false, length = 30)
	private String detailAddress;

	// @Embedded
	// private Address address;

	@Column(nullable = false, length = 13)
	private String phoneNumber;

	@Column(nullable = false)
	private boolean isDeleted;

	@Column(nullable = false, length = 50)
	private String searchKeywords;

	@Column(nullable = false, length = 255)
	private String description;

	@Column(nullable = false)
	private int capacity;

	@Column(nullable = false, length = 12)
	private String businessLicenseNo;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// @Column(nullable = false)
	// private String category;

	public Restaurant(ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest entityRequest) {
		this.name = entityRequest.getRestaurantName();
		this.position = entityRequest.getPosition();
		this.zipCode = entityRequest.getZipCode();
		this.address = entityRequest.getAddress();
		this.detailAddress = entityRequest.getDetailAddress();
		// this.address = entityRequest.getAddress();
		this.phoneNumber = entityRequest.getPhoneNumber();
		this.isDeleted = false;
		this.searchKeywords = entityRequest.getSearchKeyWords();
		this.description = entityRequest.getDescription();
		this.businessLicenseNo = entityRequest.getBusinessLicenseNo();
		this.capacity = 0;
		this.user = entityRequest.getUser();
		// this.category = entityRequest.getCategories();
	}

	//dummy data
	public Restaurant(SaveDummyRestaurantRequest request) {
		this.name = request.getName();
		this.address = request.getAddress();
		this.businessLicenseNo = String.valueOf(UUID.randomUUID());
		this.capacity = 30;
		this.description = request.getName() + "은 한국 최고의 음식 입니다.";
		this.searchKeywords = request.getCategory();
		this.phoneNumber = request.getPhoneNumber();
		this.position = request.getPosition();
		// this.category = request.getCategory();
		this.user = request.getUser();
	}

	// public String getProvince() {
	// 	return this.getAddress().getProvince();
	// }
	//
	// public String getCity() {
	// 	return this.getAddress().getCity();
	// }
	//
	// public String getStreet() {
	// 	return this.getAddress().getStreet();
	// }

	public double getLatitude() {
		return this.getPosition().getLatitude();
	}

	public double getLongitude() {
		return this.getPosition().getLongitude();
	}

	public void deleteRestaurant() {
		if (this.isDeleted) {
			throw new IllegalArgumentException("해당 유저는 이미 삭제되었습니다.");
		} else {
			this.isDeleted = true;
		}
	}

	public void updateRestaurant(UpdateRestaurantEntityRequest updateRestaurantEntityRequest) {
		this.images = updateRestaurantEntityRequest.getImages();
		this.phoneNumber = updateRestaurantEntityRequest.getPhoneNumber();
		this.capacity = updateRestaurantEntityRequest.getCapacity();
		this.description = updateRestaurantEntityRequest.getDescription();
	}
}