package team.waitingcatch.app.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeleteUserRequest {
	private final String username;
}
