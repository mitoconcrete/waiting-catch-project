package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteCategoryServiceRequest {
	private Long categoryId;

	public DeleteCategoryServiceRequest(Long categoryId) {
		this.categoryId = categoryId;
	}
}
