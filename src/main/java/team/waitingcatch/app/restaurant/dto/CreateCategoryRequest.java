package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCategoryRequest {
	private Long parentId;
	private String name;

	public CreateCategoryRequest(Long parentId, String name) {
		this.parentId = parentId;
		this.name = name;
	}
}
