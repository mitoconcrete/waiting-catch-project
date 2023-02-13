package team.waitingcatch.app.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.common.entity.TimeStamped;
import team.waitingcatch.app.restaurant.dto.ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "restaurant_id")
	private Long id;

	@Column(nullable = false)
	private String name;

	private String images;

	@Embedded
	private Position position;

	@Embedded
	private Address address;

	@Column(nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	private boolean isDeleted;

	@Column(nullable = false)
	private String searchKeywords;

	@Column(nullable = false)
	private String description;
	@Column(nullable = false)
	private Integer capacity;

	@Column(nullable = false)
	private String businessLicenseNo;

	public Restaurant(ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest
		approveSignUpSellerManagementEntityPassToRestaurantEntityRequest) {
		this.name = approveSignUpSellerManagementEntityPassToRestaurantEntityRequest.getRestaurantName();
		this.position = approveSignUpSellerManagementEntityPassToRestaurantEntityRequest.getPosition();
		this.address = approveSignUpSellerManagementEntityPassToRestaurantEntityRequest.getAddress();
		this.phoneNumber = approveSignUpSellerManagementEntityPassToRestaurantEntityRequest.getPhoneNumber();
		this.isDeleted = false;
		this.searchKeywords = approveSignUpSellerManagementEntityPassToRestaurantEntityRequest.getSearchKeyWords();
		this.description = approveSignUpSellerManagementEntityPassToRestaurantEntityRequest.getDescription();
		this.businessLicenseNo = approveSignUpSellerManagementEntityPassToRestaurantEntityRequest.getBusinessLicenseNo();
		this.capacity = 0;
	}
}
