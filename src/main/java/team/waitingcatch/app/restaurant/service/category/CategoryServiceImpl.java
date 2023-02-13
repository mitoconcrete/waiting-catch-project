package team.waitingcatch.app.restaurant.service.category;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.DeleteCategoryRequest;
import team.waitingcatch.app.restaurant.dto.UpdateCategoryServiceRequest;
import team.waitingcatch.app.restaurant.entity.Category;
import team.waitingcatch.app.restaurant.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService, InternalCategoryService {
	private final CategoryRepository categoryRepository;

	@Override
	public void createCategory(CreateCategoryRequest request) {
		Category category = Category.create(request);
		categoryRepository.save(category);
	}

	@Override
	public void updateCategory(UpdateCategoryServiceRequest serviceRequest) {
		Category category = this._getCategory(serviceRequest.getCategoryId());
		category.update(serviceRequest);
		categoryRepository.save(category);
	}

	@Override
	public void deleteCategory(DeleteCategoryRequest request) {
		Category category = this._getCategory(request.getCategoryId());
		categoryRepository.delete(category);
	}

	@Override
	public Category _getCategory(Long categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
		);
	}
}
