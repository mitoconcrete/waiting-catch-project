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

import team.waitingcatch.app.restaurant.dto.CategoryResponse;
import team.waitingcatch.app.restaurant.dto.ChildCategoryResponse;
import team.waitingcatch.app.restaurant.dto.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.DeleteCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.GetChildCategoryServiceRequest;
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

		when(request.getParentId()).thenReturn(1L);

		List<Category> categories = new ArrayList<>();
		// Category category1 = new Category(1L, null, "한식");
		// Category category2 = new Category(2L, 1L, "떡갈비");
		// Category category3 = new Category(3L, 1L, "한정식");
		// Category category4 = new Category(4L, 1L, "찌개류");
		// categories.add(category1);
		// categories.add(category2);
		// categories.add(category3);
		// categories.add(category4);

		when(categoryRepository.findAll()).thenReturn(categories);

		// when
		ChildCategoryResponse categoryResponse = categoryService.getChildCategories(request);

		// then
		// assertEquals(null, categoryResponse.getName());
		// assertEquals(null, categoryResponse.getChildCategories().get(0).getName());
		assertEquals("", categoryResponse.getName());
		assertEquals(1L, categoryResponse.getCategoryId());
		assertEquals(categories, categoryResponse.getChildCategories());
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
		verify(categoryRepository, times(1)).save(category);
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