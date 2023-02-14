package team.waitingcatch.app.restaurant.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UpdateCategoryControllerRequest {
	@NotBlank
	private String name;
}
