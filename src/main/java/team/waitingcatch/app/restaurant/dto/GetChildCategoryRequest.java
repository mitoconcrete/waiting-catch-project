package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetChildCategoryRequest {
	private Long parentId;

	public GetChildCategoryRequest(Long parentId) {
		this.parentId = parentId;
	}
}
