package team.waitingcatch.app.user.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.enums.UserRoleEnum;

@Getter
public class UserInfoResponse {
	private final Long id;
	private final String username;
	private final String email;
	private final String nickname;
	private final String name;
	private final UserRoleEnum role;
	private final Boolean isBanned;
	private final Boolean isDeleted;
	private final LocalDateTime createdDate;
	private final LocalDateTime modifiedDate;

	public UserInfoResponse(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.nickname = user.getNickname();
		this.email = user.getEmail();
		this.name = user.getName();
		this.role = user.getRole();
		this.isBanned = user.getIsBanned();
		this.isDeleted = user.getIsDeleted();
		this.createdDate = user.getCreatedDate();
		this.modifiedDate = user.getModifiedDate();
	}
}
