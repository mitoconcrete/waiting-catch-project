package team.waitingcatch.app.restaurant.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import team.waitingcatch.app.restaurant.dto.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.UpdateCategoryServiceRequest;

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
			CreateCategoryRequest request = new CreateCategoryRequest(parentId, name);

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
				CreateCategoryRequest request = new CreateCategoryRequest(parentId, name);

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
				CreateCategoryRequest request = new CreateCategoryRequest(parentId, name);

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
			UpdateCategoryServiceRequest serviceRequest = new UpdateCategoryServiceRequest(1L, "양식");
			Category category = new Category(parentId, name);

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
				UpdateCategoryServiceRequest serviceRequest = new UpdateCategoryServiceRequest(1L, name);
				Category category = new Category(parentId, "한식");

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
				UpdateCategoryServiceRequest serviceRequest = new UpdateCategoryServiceRequest(1L, name);
				Category category = new Category(parentId, "한식");

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