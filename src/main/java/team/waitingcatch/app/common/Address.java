package team.waitingcatch.app.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class Address {
	@Column(nullable = false)
	private String province;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String street;

	public Address(String province, String city, String street) {
		this.province = province;
		this.city = city;
		this.street = street;
	}
}