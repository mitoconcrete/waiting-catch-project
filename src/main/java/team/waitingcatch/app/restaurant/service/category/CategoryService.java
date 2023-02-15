package team.waitingcatch.app.restaurant.service.category;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.CategoryResponse;
import team.waitingcatch.app.restaurant.dto.ChildCategoryResponse;
import team.waitingcatch.app.restaurant.dto.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.DeleteCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.GetChildCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.UpdateCategoryServiceRequest;

public interface CategoryService {
	void createCategory(CreateCategoryRequest request);

	List<CategoryResponse> getParentCategories();

	ChildCategoryResponse getChildCategories(GetChildCategoryServiceRequest request);

	void updateCategory(UpdateCategoryServiceRequest serviceRequest);

	void deleteCategory(DeleteCategoryServiceRequest request);
}
