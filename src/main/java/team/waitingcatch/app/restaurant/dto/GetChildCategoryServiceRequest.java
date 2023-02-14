package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetChildCategoryServiceRequest {
	private Long parentId;

	public GetChildCategoryServiceRequest(Long parentId) {
		this.parentId = parentId;
	}
}
