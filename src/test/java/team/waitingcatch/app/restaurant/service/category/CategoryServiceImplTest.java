package team.waitingcatch.app.restaurant.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import team.waitingcatch.app.restaurant.dto.category.CategoryResponse;
import team.waitingcatch.app.restaurant.dto.category.ChildCategoryResponse;
import team.waitingcatch.app.restaurant.dto.category.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.category.DeleteCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.category.GetChildCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.category.UpdateCategoryServiceRequest;
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
		CreateCategoryRequest categoryRequest = mock(CreateCategoryRequest.class);

		when(categoryRequest.getParentId()).thenReturn(null);
		when(categoryRequest.getName()).thenReturn("한식");

		// when
		categoryService.createCategory(categoryRequest);

		// then
		verify(categoryRepository, times(1)).save(any(Category.class));
	}

	@Test
	@DisplayName("자식 카테고리 생성")
	void createChildCategory() {
		// given
		CreateCategoryRequest categoryRequest = mock(CreateCategoryRequest.class);

		when(categoryRequest.getParentId()).thenReturn(1L);
		when(categoryRequest.getName()).thenReturn("떡갈비");

		// when
		categoryService.createCategory(categoryRequest);

		// then
		verify(categoryRepository, times(1)).save(any(Category.class));
	}

	@Test
	@DisplayName("최상위 카테고리 조회")
	void getParentCategory() {
		// given
		List<Category> categories = new ArrayList<>();
		Category category1 = new Category(null, "한식");
		Category category2 = new Category(null, "양식");
		categories.add(category1);
		categories.add(category2);

		when(categoryRepository.findAllByParentId(null)).thenReturn(categories);

		// when
		List<CategoryResponse> responses = categoryService.getParentCategories();

		// then
		assertEquals("한식", responses.get(0).getName());
		assertEquals("양식", responses.get(1).getName());
	}

	@Test
	@DisplayName("하위 카테고리 조회")
	void getChildCategories() {
		// given
		GetChildCategoryServiceRequest request = mock(GetChildCategoryServiceRequest.class);
		Category category1 = mock(Category.class);
		Category category2 = mock(Category.class);

		when(request.getParentId()).thenReturn(1L);

		when(category1.getId()).thenReturn(1L);
		when(category1.getParentId()).thenReturn(null);
		when(category1.getName()).thenReturn("parent");

		when(category2.getId()).thenReturn(2L);
		when(category2.getParentId()).thenReturn(1L);
		when(category2.getName()).thenReturn("child");

		List<Category> categories = new ArrayList<>();
		categories.add(category1);
		categories.add(category2);

		when(categoryRepository.findAll()).thenReturn(categories);

		// when
		ChildCategoryResponse categoryResponse = categoryService.getChildCategories(request);

		// then
		assertEquals("parent", categoryResponse.getName());
		assertEquals("child", categoryResponse.getChildCategories().get(0).getName());
	}

	@Test
	@DisplayName("카테고리 수정")
	void updateCategory() {
		// given
		UpdateCategoryServiceRequest serviceRequest = mock(UpdateCategoryServiceRequest.class);
		Category category = new Category(null, "한식");

		when(serviceRequest.getName()).thenReturn("양식");
		when(categoryRepository.findById(serviceRequest.getCategoryId())).thenReturn(Optional.of(category));

		// when
		categoryService.updateCategory(serviceRequest);

		// then
		assertEquals("양식", category.getName());
	}

	@Test
	@DisplayName("카테고리 삭제")
	void deleteCategory() {
		// given
		DeleteCategoryServiceRequest request = mock(DeleteCategoryServiceRequest.class);
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
		Category category1 = categoryService._getCategory(any(Long.class));

		// then
		assertNull(category1.getId());
		assertEquals("한식", category1.getName());
	}
}