package team.waitingcatch.app.restaurant.dto.category;

import lombok.Getter;

@Getter
public class GetChildCategoryServiceRequest {
	private final Long parentId;

	public GetChildCategoryServiceRequest(Long parentId) {
		this.parentId = parentId;
	}
}
