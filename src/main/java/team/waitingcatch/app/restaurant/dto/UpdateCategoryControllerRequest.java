package team.waitingcatch.app.restaurant.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UpdateCategoryControllerRequest {
	@NotBlank(message = "올바른 카테고리명이 아닙니다.")
	private String name;
}
