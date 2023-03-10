package team.waitingcatch.app.restaurant.dto.category;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Category;

@Getter
public class CategoryResponse {
	private final Long id;
	private final String name;

	private final Long parentId;

	public CategoryResponse(Category category) {
		this.id = category.getId();
		this.name = category.getName();
		this.parentId = category.getParentId();
	}
}
