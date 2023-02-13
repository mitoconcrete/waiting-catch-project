package team.waitingcatch.app.restaurant.service.category;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.CategoryResponse;
import team.waitingcatch.app.restaurant.dto.ChildCategoryResponse;
import team.waitingcatch.app.restaurant.dto.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.DeleteCategoryRequest;
import team.waitingcatch.app.restaurant.dto.GetChildCategoryRequest;
import team.waitingcatch.app.restaurant.dto.UpdateCategoryServiceRequest;

public interface CategoryService {
	void createCategory(CreateCategoryRequest request);

	List<CategoryResponse> getParentCategories();

	ChildCategoryResponse getChildCategories(GetChildCategoryRequest request);

	void updateCategory(UpdateCategoryServiceRequest serviceRequest);

	void deleteCategory(DeleteCategoryRequest request);
}
