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
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SellerManagement extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seller_management_id")
	private Long id;
	@Column(nullable = false)
	private String username;
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
	@Column(name = "business_license_no", nullable = false)
	private String businessLicenseNo;
	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private AcceptedStatusEnum status;
	@Column(nullable = false)
	private String categories;
	@Column(name = "search_key_words", nullable = false)
	private String searchKeyWords;

}
