package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCategoryServiceRequest {
	private Long categoryId;
	private String name;

	public UpdateCategoryServiceRequest(Long categoryId, String name) {
		this.categoryId = categoryId;
		this.name = name;
	}
}
