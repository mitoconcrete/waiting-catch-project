package team.waitingcatch.app.restaurant.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.service.category.CategoryService;

@RestController
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;

	@PostMapping("/admin/categories")
	public void createCategory(@RequestBody CreateCategoryRequest request) {
		categoryService.createCategory(request);
	}
}
