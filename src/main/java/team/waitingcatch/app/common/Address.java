package team.waitingcatch.app.common;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
	@Column(nullable = false)
	private String province;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String street;
}
