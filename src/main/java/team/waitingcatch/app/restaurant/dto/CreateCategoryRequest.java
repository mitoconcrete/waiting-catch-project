package team.waitingcatch.app.restaurant.dto;

import lombok.Getter;

@Getter
public class CreateCategoryRequest {
	private Long parentId;
	private String name;
}
