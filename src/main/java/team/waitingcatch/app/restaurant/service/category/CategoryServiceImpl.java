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
import team.waitingcatch.app.restaurant.dto.requestseller.ConnectCategoryRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.entity.Category;
import team.waitingcatch.app.restaurant.entity.CategoryRestaurant;
import team.waitingcatch.app.restaurant.repository.CategoryRepository;
import team.waitingcatch.app.restaurant.repository.CategoryRestaurantRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService, InternalCategoryService {
	private final CategoryRepository categoryRepository;
	private final CategoryRestaurantRepository categoryRestaurantRepository;

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
	public List<CategoryResponse> getChildCategoriesForSellerManagement(Long parentId) {
		return categoryRepository.findAllByParentId(parentId).stream()
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
	public void _connectCategoryRestaurant(ConnectCategoryRestaurantServiceRequest serviceRequest) {
		List<Long> categoryIds = serviceRequest.getCategoryIds().stream()
			.map(Long::parseLong)
			.collect(Collectors.toList());
		List<Category> categories = categoryRepository.findAllByIdIn(categoryIds);

		for (Category category : categories) {
			CategoryRestaurant categoryRestaurant = new CategoryRestaurant(category, serviceRequest.getRestaurant());
			categoryRestaurantRepository.save(categoryRestaurant);
		}
	}

	@Override
	public void _connectCategoryRestaurantDummy(ConnectCategoryRestaurantServiceRequest serviceRequest) {
		List<Category> categories = categoryRepository.findAllByName(serviceRequest.getCategoryIds());
		System.out.println("1 = " + 1);
		for (Category category : categories) {
			System.out.println("2 = " + 2);

			CategoryRestaurant categoryRestaurant = new CategoryRestaurant(category, serviceRequest.getRestaurant());
			System.out.println("3 = " + 3);

			categoryRestaurantRepository.save(categoryRestaurant);
			System.out.println("4 = " + 4);

		}
	}

	@Override
	public List<String> _getCategoryNames(List<Long> categoryIds) {
		return categoryRepository.findNameByIdIn(categoryIds);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryResponse> getAllCategories() {
		return categoryRepository.findAll().stream()
			.map(CategoryResponse::new)
			.collect(Collectors.toList());
	}

}
