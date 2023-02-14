package team.waitingcatch.app.restaurant.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CreateCategoryRequest {
	private Long parentId;
	@NotBlank
	private String name;
}
