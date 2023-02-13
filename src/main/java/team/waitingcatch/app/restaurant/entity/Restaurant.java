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
	private String capacity;

	@Column(nullable = false)
	private String businessLicenseNo;
}
