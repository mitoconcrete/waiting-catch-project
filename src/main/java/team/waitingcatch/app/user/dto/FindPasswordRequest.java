package team.waitingcatch.app.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
public class FindPasswordRequest {
	@NonNull
	private String username;

	@NonNull
	private String email;
}
