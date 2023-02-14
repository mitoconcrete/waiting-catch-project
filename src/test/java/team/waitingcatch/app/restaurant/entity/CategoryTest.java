package team.waitingcatch.app.restaurant.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import team.waitingcatch.app.restaurant.dto.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.UpdateCategoryServiceRequest;

@ExtendWith(MockitoExtension.class)
class CategoryTest {

	@Nested
	@DisplayName("카테고리 생성")
	class CreateCategory {

		private Long id;
		private Long parentId;
		private String name;

		@BeforeEach
		void setup() {
			parentId = null;
			name = "한식";
		}

		@Test
		@DisplayName("정상 케이스")
		void createCategory_Normal() {
			// given
			CreateCategoryRequest request = mock(CreateCategoryRequest.class);

			when(request.getParentId()).thenReturn(parentId);
			when(request.getName()).thenReturn(name);

			// when
			Category category = Category.create(request);

			// then
			assertNull(category.getId());
			assertEquals(parentId, category.getParentId());
			assertEquals(name, category.getName());
		}

		@Nested
		@DisplayName("실패 케이스")
		class FailCases {

			@Test
			@DisplayName("카테고리명이 null인 경우")
			void fail1() {
				//given
				name = null;
				CreateCategoryRequest request = mock(CreateCategoryRequest.class);

				when(request.getParentId()).thenReturn(parentId);
				when(request.getName()).thenReturn(name);

				// when
				Exception exception = assertThrows(IllegalArgumentException.class, () -> {
					Category.create(request);
				});

				// then
				assertEquals("카테고리명을 입력하세요.", exception.getMessage());
			}

			@Test
			@DisplayName("카테고리명이 빈 문자열인 경우")
			void fail2() {
				// given
				name = "";
				CreateCategoryRequest request = mock(CreateCategoryRequest.class);

				when(request.getParentId()).thenReturn(parentId);
				when(request.getName()).thenReturn(name);

				// when
				Exception exception = assertThrows(IllegalArgumentException.class, () -> {
					Category.create(request);
				});

				// then
				assertEquals("카테고리명에 빈값을 입력할 수 없습니다.", exception.getMessage());
			}
		}
	}

	@Nested
	@DisplayName("카테고리 수정")
	class updateCategory {

		private Long id;
		private Long parentId;
		private String name;

		@BeforeEach
		void setup() {
			parentId = null;
			name = "한식";
		}

		@Test
		@DisplayName("정상 케이스")
		void updateCategory_Normal() {
			// given
			UpdateCategoryServiceRequest serviceRequest = mock(UpdateCategoryServiceRequest.class);
			Category category = new Category(parentId, name);

			when(serviceRequest.getName()).thenReturn("양식");

			// when
			category.update(serviceRequest);

			// then
			assertEquals(serviceRequest.getName(), category.getName());
		}

		@Nested
		@DisplayName("실패 케이스")
		class FailCases {

			@Test
			@DisplayName("카테고리명이 null인 경우")
			void fail1() {
				//given
				name = null;
				UpdateCategoryServiceRequest serviceRequest = mock(UpdateCategoryServiceRequest.class);
				Category category = new Category(parentId, "한식");

				when(serviceRequest.getName()).thenReturn(name);

				// when
				Exception exception = assertThrows(IllegalArgumentException.class, () -> {
					category.update(serviceRequest);
				});

				// then
				assertEquals("카테고리명을 입력하세요.", exception.getMessage());
			}

			@Test
			@DisplayName("카테고리명이 빈값인 경우")
			void fail2() {
				//given
				name = "";
				UpdateCategoryServiceRequest serviceRequest = mock(UpdateCategoryServiceRequest.class);
				Category category = new Category(parentId, "한식");

				when(serviceRequest.getName()).thenReturn(name);

				// when
				Exception exception = assertThrows(IllegalArgumentException.class, () -> {
					category.update(serviceRequest);
				});

				// then
				assertEquals("카테고리명에 빈값을 입력할 수 없습니다.", exception.getMessage());
			}
		}
	}
}