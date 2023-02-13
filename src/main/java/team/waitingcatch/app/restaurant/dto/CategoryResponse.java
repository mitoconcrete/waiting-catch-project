package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.restaurant.entity.Category;

@Getter
@NoArgsConstructor
public class CategoryResponse {
	private Long id;
	private String name;

	public CategoryResponse(Category category) {
		this.id = category.getId();
		this.name = category.getName();
	}
}
