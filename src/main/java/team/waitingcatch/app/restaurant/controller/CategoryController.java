package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.CategoryResponse;
import team.waitingcatch.app.restaurant.dto.ChildCategoryResponse;
import team.waitingcatch.app.restaurant.dto.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.DeleteCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.GetChildCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.UpdateCategoryControllerRequest;
import team.waitingcatch.app.restaurant.dto.UpdateCategoryServiceRequest;
import team.waitingcatch.app.restaurant.service.category.CategoryService;

@RestController
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;

	@PostMapping("/admin/categories")
	public void createCategory(@RequestBody CreateCategoryRequest request) {
		categoryService.createCategory(request);
	}

	@GetMapping("/admin/categories")
	public List<CategoryResponse> getParentCategories() {
		return categoryService.getParentCategories();
	}

	@GetMapping("/admin/categories/{categoryId}")
	public ChildCategoryResponse getChildCategories(@PathVariable Long categoryId) {
		GetChildCategoryServiceRequest request = new GetChildCategoryServiceRequest(categoryId);
		return categoryService.getChildCategories(request);
	}

	@PutMapping("/admin/categories/{categoryId}")
	public void updateCategory(@PathVariable Long categoryId,
		@RequestBody UpdateCategoryControllerRequest controllerRequest) {

		UpdateCategoryServiceRequest serviceRequest =
			new UpdateCategoryServiceRequest(categoryId, controllerRequest.getName());

		categoryService.updateCategory(serviceRequest);
	}

	@DeleteMapping("/admin/categories/{categoryId}")
	public void deleteCategory(@PathVariable Long categoryId) {
		DeleteCategoryServiceRequest request = new DeleteCategoryServiceRequest(categoryId);
		categoryService.deleteCategory(request);
	}

}
