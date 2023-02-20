package team.waitingcatch.app.restaurant.dto.restaurant;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateRestaurantControllerRequest {
	@NotNull
	private String phoneNumber;
	@NotNull
	private int capacity;
	@NotNull
	private String description;
	@NotNull
	private String openTime;
	@NotNull
	private String closeTime;
}
