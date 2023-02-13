package team.waitingcatch.app.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.common.entity.TimeStamped;
import team.waitingcatch.app.restaurant.dto.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SellerManagement extends TimeStamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seller_management_id")
	private Long id;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String email;
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;
	@Column(name = "restaurant_name", nullable = false)
	private String restaurantName;
	@Embedded
	private Address address;
	@Embedded
	private Position position;
	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private AcceptedStatusEnum status;
	@Column(nullable = false)
	private String categories;
	@Column(name = "search_keywords", nullable = false)
	private String searchKeyWords;
	@Column(name = "business_license_no", nullable = false)
	private String businessLicenseNo;

	public SellerManagement(DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest) {
		this.username = demandSignupSellerServiceRequest.getUsername();
		this.email = demandSignupSellerServiceRequest.getEmail();
		this.phoneNumber = demandSignupSellerServiceRequest.getPhoneNumber();
		this.restaurantName = demandSignupSellerServiceRequest.getRestaurantName();
		this.address = demandSignupSellerServiceRequest.getAddress();
		this.position = demandSignupSellerServiceRequest.getPosition();
		this.description = demandSignupSellerServiceRequest.getDescription();
		this.status = AcceptedStatusEnum.WAIT;
		this.categories = demandSignupSellerServiceRequest.getCategories();
		this.searchKeyWords = demandSignupSellerServiceRequest.getSearchKeyWords();
		this.businessLicenseNo = demandSignupSellerServiceRequest.getBusinessLicenseNo();
		this.name = demandSignupSellerServiceRequest.getName();
	}

	public void approveUpdateStatus() {
		this.status = AcceptedStatusEnum.APPROVAL;
	}

	public void rejectUpdateStatus() {

		this.status = AcceptedStatusEnum.REJECTION;
	}

}
