package team.waitingcatch.app.restaurant.dto.menu;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CreateMenuControllerRequest {
	@NotBlank
	private String name;
	@NotBlank
	private int price;
}
