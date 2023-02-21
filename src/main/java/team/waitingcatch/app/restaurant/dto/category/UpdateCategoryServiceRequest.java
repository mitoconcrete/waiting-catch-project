package team.waitingcatch.app.restaurant.dto.category;

import lombok.Getter;

@Getter
public class UpdateCategoryServiceRequest {
	private final Long categoryId;
	private final String name;

	public UpdateCategoryServiceRequest(Long categoryId, String name) {
		this.categoryId = categoryId;
		this.name = name;
	}
}
