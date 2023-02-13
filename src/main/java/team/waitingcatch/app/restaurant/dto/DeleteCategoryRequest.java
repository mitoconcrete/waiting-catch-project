package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteCategoryRequest {
	private Long categoryId;

	public DeleteCategoryRequest(Long categoryId) {
		this.categoryId = categoryId;
	}
}
