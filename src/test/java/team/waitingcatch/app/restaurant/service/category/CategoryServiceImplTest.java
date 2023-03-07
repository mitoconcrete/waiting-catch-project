package team.waitingcatch.app.restaurant.service.category;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
import team.waitingcatch.app.restaurant.dto.requestseller.ConnectCategoryRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.entity.Category;
import team.waitingcatch.app.restaurant.entity.CategoryRestaurant;
import team.waitingcatch.app.restaurant.repository.CategoryRepository;
import team.waitingcatch.app.restaurant.repository.CategoryRestaurantRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private CategoryRestaurantRepository categoryRestaurantRepository;

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
	@DisplayName("판매자요청 페이지 하위 카테고리 조회")
	void getChildCategoryForSellerManagement() {
		// given
		List<Category> categories = new ArrayList<>();
		Category category1 = mock(Category.class);
		Category category2 = mock(Category.class);
		categories.add(category1);
		categories.add(category2);

		when(category1.getParentId()).thenReturn(1L);
		when(category1.getName()).thenReturn("한식1");
		when(category2.getParentId()).thenReturn(1L);
		when(category2.getName()).thenReturn("한식2");
		when(categoryRepository.findAllByParentId(any(Long.class))).thenReturn(categories);

		// when
		List<CategoryResponse> responses = categoryService.getChildCategoriesForSellerManagement(1L);

		// then
		assertEquals("한식1", responses.get(0).getName());
		assertEquals("한식2", responses.get(1).getName());

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

	@Test
	@DisplayName("카테고리, 레스토랑 매핑")
	void connectCategoryRestaurant() {
		// given
		ConnectCategoryRestaurantServiceRequest request = mock(ConnectCategoryRestaurantServiceRequest.class);
		List<Category> categories = new ArrayList<>();
		Category category1 = mock(Category.class);
		Category category2 = mock(Category.class);
		categories.add(category1);
		categories.add(category2);
		// List<Long> categoryIds = new ArrayList<>();

		// when(request.getCategoryIds()).thenReturn("1 2");
		when(categoryRepository.findAllByIdIn(any(List.class))).thenReturn(categories);

		// when
		categoryService._connectCategoryRestaurant(request);

		// then
		verify(categoryRestaurantRepository, times(2)).save(any(CategoryRestaurant.class));
	}

	@Test
	@DisplayName("Internal 카테고리명 조회")
	void _getCategoryNames() {
		// given
		List<Long> categoryIds = new ArrayList<>();
		categoryIds.add(1L);
		categoryIds.add(2L);
		List<String> categoryNames = new ArrayList<>();
		categoryNames.add("한식");
		categoryNames.add("중식");

		when(categoryRepository.findNameByIdIn(categoryIds)).thenReturn(categoryNames);

		// when
		List<String> categoryNames1 = categoryService._getCategoryNames(categoryIds);

		// then
		assertEquals("한식", categoryNames1.get(0));
		assertEquals("중식", categoryNames1.get(1));
	}

	@Test
	@DisplayName("Internal 모든 카테고리 조회")
	void getAllCategories() {
		// given
		Category category1 = mock(Category.class);
		Category category2 = mock(Category.class);
		List<Category> categories = new ArrayList<>();
		categories.add(category1);
		categories.add(category2);

		when(category1.getId()).thenReturn(1L);
		when(category1.getName()).thenReturn("한식");
		when(category2.getId()).thenReturn(2L);
		when(category2.getName()).thenReturn("중식");
		when(categoryRepository.findAll()).thenReturn(categories);

		// when
		List<CategoryResponse> responses = categoryService.getAllCategories();

		// then
		assertEquals("한식", responses.get(0).getName());
		assertEquals("중식", responses.get(1).getName());
	}
}