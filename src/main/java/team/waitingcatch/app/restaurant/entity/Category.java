package team.waitingcatch.app.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.restaurant.dto.CreateCategoryRequest;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;

	private Long parentId;

	@Column(nullable = false)
	private String name;

	public Category(Long parentId, String name) {
		if (name == null)
			throw new IllegalArgumentException("카테고리명을 입력하세요.");
		if (name.equals(""))
			throw new IllegalArgumentException("카테고리명에 빈값을 입력할 수 없습니다.");

		this.parentId = parentId;
		this.name = name;
	}

	public static Category create(CreateCategoryRequest request) {
		return new Category(request.getParentId(), request.getName());
	}

}
