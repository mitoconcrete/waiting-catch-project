package team.waitingcatch.app.restaurant.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import team.waitingcatch.app.restaurant.dto.DeleteCategoryRequest;
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

	@Test
	@DisplayName("카테고리 삭제")
	void deleteCategory() {
		// given
		DeleteCategoryRequest request = new DeleteCategoryRequest(any(Long.class));
		Category category = new Category(null, "한식");

		when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));

		// when
		categoryService.deleteCategory(request);

		// then
		verify(categoryRepository, times(1)).delete(category);
	}

	@Test
	@DisplayName("Internal 카테고리 조회")
	void _getCategory() {
		// given
		Category category = new Category(null, "한식");

		when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));

		// when
		categoryService._getCategory(any(Long.class));

		// then
		assertNull(category.getId());
		assertEquals("한식", category.getName());
	}
}