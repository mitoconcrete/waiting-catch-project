package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlacklistDemandControllerRequest {
	@NotNull
	private long userId;

	@NotNull
	private String description;
}