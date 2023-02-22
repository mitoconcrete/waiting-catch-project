package team.waitingcatch.app.restaurant.service.category;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.category.CategoryResponse;
import team.waitingcatch.app.restaurant.dto.category.ChildCategoryResponse;
import team.waitingcatch.app.restaurant.dto.category.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.category.DeleteCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.category.GetChildCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.category.UpdateCategoryServiceRequest;

public interface CategoryService {
	void createCategory(CreateCategoryRequest request);

	List<CategoryResponse> getParentCategories();

	ChildCategoryResponse getChildCategories(GetChildCategoryServiceRequest request);

	void updateCategory(UpdateCategoryServiceRequest serviceRequest);

	void deleteCategory(DeleteCategoryServiceRequest request);

	List<CategoryResponse> getAllCategories();

}
