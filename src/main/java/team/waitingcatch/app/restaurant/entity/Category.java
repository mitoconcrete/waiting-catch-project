package team.waitingcatch.app.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.restaurant.dto.category.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.category.UpdateCategoryServiceRequest;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;

	private Long parentId;

	@Column(nullable = false, length = 15)
	private String name;

	public Category(Long parentId, String name) {
		this.parentId = parentId;
		this.name = name;
	}

	public static Category create(CreateCategoryRequest request) {
		return new Category(request.getParentId(), request.getName());
	}

	public void update(UpdateCategoryServiceRequest serviceRequest) {
		this.name = serviceRequest.getName();
	}
}
