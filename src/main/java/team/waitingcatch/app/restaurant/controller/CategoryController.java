package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.category.CategoryResponse;
import team.waitingcatch.app.restaurant.dto.category.ChildCategoryResponse;
import team.waitingcatch.app.restaurant.dto.category.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.category.DeleteCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.category.GetChildCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.category.UpdateCategoryControllerRequest;
import team.waitingcatch.app.restaurant.dto.category.UpdateCategoryServiceRequest;
import team.waitingcatch.app.restaurant.service.category.CategoryService;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;

	@PostMapping("/admin/categories")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createCategory(@RequestBody @Valid CreateCategoryRequest request) {
		categoryService.createCategory(request);
	}

	@GetMapping("/general/categories")
	public List<CategoryResponse> getParentCategoriesForSellerManagement() {
		return categoryService.getParentCategories();
	}

	@GetMapping("/admin/categories")
	public List<CategoryResponse> getParentCategories() {
		return categoryService.getParentCategories();
	}

	@GetMapping("/general/categories/{categoryId}")
	public List<CategoryResponse> getChildCategoriesForSellerManagement(@PathVariable Long categoryId) {
		return categoryService.getChildCategoriesForSellerManagement(categoryId);
	}

	@GetMapping("/admin/categories/{categoryId}")
	public ChildCategoryResponse getChildCategories(@PathVariable Long categoryId) {
		GetChildCategoryServiceRequest request = new GetChildCategoryServiceRequest(categoryId);
		return categoryService.getChildCategories(request);
	}

	@PutMapping("/admin/categories/{categoryId}")
	public void updateCategory(@PathVariable Long categoryId,
		@RequestBody @Valid UpdateCategoryControllerRequest controllerRequest) {

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
