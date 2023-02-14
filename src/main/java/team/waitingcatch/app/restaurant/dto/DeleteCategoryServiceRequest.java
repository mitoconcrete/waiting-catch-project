package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;

@Getter
public class DeleteCategoryServiceRequest {
	private final Long categoryId;

	public DeleteCategoryServiceRequest(Long categoryId) {
		this.categoryId = categoryId;
	}
}
