package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCategoryControllerRequest {
	private String name;

	public UpdateCategoryControllerRequest(String name) {
		this.name = name;
	}
}
