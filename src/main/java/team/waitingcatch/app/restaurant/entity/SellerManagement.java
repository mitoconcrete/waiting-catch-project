package team.waitingcatch.app.restaurant.entity;

import java.util.ArrayList;

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

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	private String restaurantName;

	@Column(nullable = false)
	private String zipCode;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private String detailAddress;

	// @Embedded
	// private Address address;

	@Embedded
	private Position position;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private AcceptedStatusEnum status;

	@Column(nullable = false)
	@Convert(converter = StringListConverter.class)
	private List<String> categories;

	// @Column(name = "search_keywords", nullable = false)
	// private String searchKeyWords;

	@Column(nullable = false)
	private String businessLicenseNo;

	public SellerManagement(DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest) {
		this.username = demandSignupSellerServiceRequest.getUsername();
		this.email = demandSignupSellerServiceRequest.getEmail();
		this.phoneNumber = demandSignupSellerServiceRequest.getPhoneNumber();
		this.restaurantName = demandSignupSellerServiceRequest.getRestaurantName();
		this.zipCode = demandSignupSellerServiceRequest.getZipCode();
		this.address = demandSignupSellerServiceRequest.getAddress();
		this.detailAddress = demandSignupSellerServiceRequest.getDetailAddress();
		// this.address = demandSignupSellerServiceRequest.getAddress();
		this.position = demandSignupSellerServiceRequest.getPosition();
		this.description = demandSignupSellerServiceRequest.getDescription();
		this.status = AcceptedStatusEnum.WAIT;
		this.categories = demandSignupSellerServiceRequest.getCategories();
		// this.searchKeyWords = "";
		this.businessLicenseNo = demandSignupSellerServiceRequest.getBusinessLicenseNo();
		this.name = demandSignupSellerServiceRequest.getName();
	}

	public void approveUpdateStatus() {
		this.status = AcceptedStatusEnum.APPROVE;
	}

	public void rejectUpdateStatus() {
		this.status = AcceptedStatusEnum.REJECT;
	}

	public void checkReject() {
		if (this.status == AcceptedStatusEnum.REJECT) {
			throw new IllegalArgumentException("This request already reject please request seller again");
		}
	}

	public void checkApprove() {
		if (this.status == AcceptedStatusEnum.APPROVE) {
			throw new IllegalArgumentException("This request already Approve please request seller again");
		}
	}
}