package team.waitingcatch.app.restaurant.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
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
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.common.entity.TimeStamped;
import team.waitingcatch.app.common.util.StringListConverter;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SellerManagement extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seller_management_id")
	private Long id;

	@Column(nullable = false, length = 100)
	private String restaurantName;

	@Column(nullable = false)
	@Convert(converter = StringListConverter.class)
	private List<String> categories;

	@Column(nullable = false, length = 200)
	private String description;

	@Column(nullable = false, length = 30)
	private String name;

	@Column(nullable = false, length = 12)
	private String businessLicenseNo;

	@Column(nullable = false, length = 20)
	private String username;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false, length = 13)
	private String phoneNumber;

	@Column(nullable = false, length = 50)
	private String address;

	@Column(nullable = false, length = 30)
	private String detailAddress;

	@Column(nullable = false, length = 5)
	private String zipCode;

	@Column(nullable = false, length = 20)
	@Enumerated(value = EnumType.STRING)
	private AcceptedStatusEnum status;

	@Embedded
	private Position position;

	public SellerManagement(DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest) {
		this.username = demandSignupSellerServiceRequest.getUsername();
		this.email = demandSignupSellerServiceRequest.getEmail();
		this.phoneNumber = demandSignupSellerServiceRequest.getPhoneNumber();
		this.restaurantName = demandSignupSellerServiceRequest.getRestaurantName();
		this.zipCode = demandSignupSellerServiceRequest.getZipCode();
		this.address = demandSignupSellerServiceRequest.getAddress();
		this.detailAddress = demandSignupSellerServiceRequest.getDetailAddress();
		this.position = demandSignupSellerServiceRequest.getPosition();
		this.description = demandSignupSellerServiceRequest.getDescription();
		this.status = AcceptedStatusEnum.WAIT;
		this.categories = demandSignupSellerServiceRequest.getCategories();
		this.businessLicenseNo = demandSignupSellerServiceRequest.getBusinessLicenseNo();
		this.name = demandSignupSellerServiceRequest.getName();
	}

	public void approveUpdateStatus() {
		this.status = AcceptedStatusEnum.APPROVE;
	}

	public void rejectUpdateStatus() {
		this.status = AcceptedStatusEnum.REJECT;
	}

	public boolean checkReject() {
		return this.status != AcceptedStatusEnum.REJECT;
	}

	public boolean checkApprove() {
		return this.status != AcceptedStatusEnum.APPROVE;

	}
}