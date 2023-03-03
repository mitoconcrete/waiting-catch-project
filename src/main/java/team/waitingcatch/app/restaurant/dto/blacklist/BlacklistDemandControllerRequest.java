package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BlacklistDemandControllerRequest {
	@NotNull
	private Long userId;

	@NotNull
	@Size(max = 100)
	private String description;
}