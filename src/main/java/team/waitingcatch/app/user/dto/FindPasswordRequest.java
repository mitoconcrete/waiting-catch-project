package team.waitingcatch.app.user.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindPasswordRequest {
	@NotNull
	private String username;

	@NotNull
	private String email;
}
