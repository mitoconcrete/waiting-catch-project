package team.waitingcatch.app.restaurant.dto.menu;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UpdateMenuControllerRequest {
	@NotBlank
	private String name;
	@NotBlank
	private int price;
}
