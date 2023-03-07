package team.waitingcatch.app.restaurant.dto.menu;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMenuControllerRequest {
	@NotBlank
	private String name;
	private int price;
}
