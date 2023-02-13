package team.waitingcatch.app.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Position {
	@Column(nullable = false)
	private float latitude;

	@Column(nullable = false)
	private float longitude;

	public Position(float latitude, float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
