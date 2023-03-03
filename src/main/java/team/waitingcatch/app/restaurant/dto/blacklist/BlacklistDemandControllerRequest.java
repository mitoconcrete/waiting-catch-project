package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlacklistDemandControllerRequest {
	@NotNull
	private long userId;

	@NotNull
	@Size(max = 100)
	private String description;
}