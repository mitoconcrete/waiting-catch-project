package team.waitingcatch.app.user.dto;

import lombok.Getter;

@Getter
public class AccessTokenResonse {
	private final String accessToken;

	public AccessTokenResonse(String accessToken) {
		this.accessToken = accessToken;
	}
}
