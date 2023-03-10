package team.waitingcatch.app.user.dto;

import lombok.Getter;

@Getter
public class GetUserInfo {
	private final String name;
	private final String email;
	private final String username;
	private final String nickName;
	private final String phoneNumber;

	public GetUserInfo(String name, String email, String username, String nickName, String phoneNumber) {
		this.name = name;
		this.email = email;
		this.username = username;
		this.nickName = nickName;
		this.phoneNumber = phoneNumber;
	}
}
