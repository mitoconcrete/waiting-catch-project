package team.waitingcatch.app.restaurant.service.category;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.category.CategoryResponse;
import team.waitingcatch.app.restaurant.dto.category.ChildCategoryResponse;
import team.waitingcatch.app.restaurant.dto.category.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.category.DeleteCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.category.GetChildCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.category.UpdateCategoryServiceRequest;
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
	@Transactional(readOnly = true)
	public List<CategoryResponse> getParentCategories() {
		return categoryRepository.findAllByParentId(null).stream()
			.map(CategoryResponse::new)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ChildCategoryResponse getChildCategories(GetChildCategoryServiceRequest request) {
		List<Category> categories = categoryRepository.findAll();

		return new ChildCategoryResponse(categories, request.getParentId());
	}

	@Override
	public void updateCategory(UpdateCategoryServiceRequest serviceRequest) {
		Category category = _getCategory(serviceRequest.getCategoryId());
		category.update(serviceRequest);
	}

	@Override
	public void deleteCategory(DeleteCategoryServiceRequest request) {
		Category category = _getCategory(request.getCategoryId());
		categoryRepository.delete(category);
	}

	@Override
	@Transactional(readOnly = true)
	public Category _getCategory(Long categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
		);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryResponse> getAllCategories() {
		return categoryRepository.findAll().stream()
			.map(CategoryResponse::new)
			.collect(Collectors.toList());
	}

}
