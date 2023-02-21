package team.waitingcatch.app.restaurant.dto.category;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CreateCategoryRequest {
	private Long parentId;
	@NotBlank(message = "올바른 카테고리명이 아닙니다.")
	private String name;
}
