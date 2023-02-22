package team.waitingcatch.app.restaurant.dto.category;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategoryControllerRequest {
	private Long categoryId;
	@NotBlank(message = "올바른 카테고리명이 아닙니다.")
	private String name;
}
