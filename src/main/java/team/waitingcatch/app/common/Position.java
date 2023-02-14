package team.waitingcatch.app.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;

@Embeddable
@Getter
public class Position {
	@Column(nullable = false)
	private float latitude;

	@Column(nullable = false)
	private float longitude;
}
