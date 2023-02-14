package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;

@Getter
public class GetChildCategoryServiceRequest {
	private final Long parentId;

	public GetChildCategoryServiceRequest(Long parentId) {
		this.parentId = parentId;
	}
}
