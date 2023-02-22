package team.waitingcatch.app.restaurant.dto.blacklist;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DemandBlacklistByRestaurantControllerRequest {
	@NotNull
	private Long userId;

	@NotNull
	private String description;
}