package team.waitingcatch.app.restaurant.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import team.waitingcatch.app.restaurant.entity.Category;

@Getter
public class ChildCategoryResponse {
	private final Long categoryId;
	private final String name;
	private final List<ChildCategoryResponse> childCategories;

	public ChildCategoryResponse(List<Category> categories, Long categoryId) {
		this.categoryId = categoryId;

		String name = "";
		for (Category category : categories) {
			if (Objects.equals(category.getId(), categoryId)) {
				name = category.getName();
			}
		}
		this.name = name;

		List<ChildCategoryResponse> categoryResponses = new ArrayList<>();
		for (Category category : categories) {
			if (Objects.equals(category.getParentId(), categoryId)) {
				categoryResponses.add(new ChildCategoryResponse(categories, category.getId()));
			}
		}
		this.childCategories = categoryResponses;
	}
}
