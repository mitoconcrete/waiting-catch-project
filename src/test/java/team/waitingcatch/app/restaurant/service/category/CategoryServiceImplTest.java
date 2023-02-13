package team.waitingcatch.app.restaurant.service.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import team.waitingcatch.app.restaurant.dto.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.UpdateCategoryServiceRequest;
import team.waitingcatch.app.restaurant.entity.Category;
import team.waitingcatch.app.restaurant.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private CategoryServiceImpl categoryService;

	@Test
	@DisplayName("부모 카테고리 생성")
	void createParentCategory() {
		// given
		CreateCategoryRequest categoryRequest = new CreateCategoryRequest(null, "한식");

		// when
		categoryService.createCategory(categoryRequest);

		// then
		verify(categoryRepository, times(1)).save(any(Category.class));
	}

	@Test
	@DisplayName("자식 카테고리 생성")
	void createChildCategory() {
		// given
		CreateCategoryRequest categoryRequest = new CreateCategoryRequest(1L, "떡갈비");

		// when
		categoryService.createCategory(categoryRequest);

		// then
		verify(categoryRepository, times(1)).save(any(Category.class));
	}

	@Test
	@DisplayName("카테고리 수정")
	void updateCategory() {
		// given
		UpdateCategoryServiceRequest serviceRequest = new UpdateCategoryServiceRequest(1L, "양식");
		Category category = new Category(null, "한식");

		when(categoryRepository.findById(serviceRequest.getCategoryId())).thenReturn(Optional.of(category));

		// when
		categoryService.updateCategory(serviceRequest);

		// then
		verify(categoryRepository, times(1)).save(category);
	}
}