package team.waitingcatch.app.restaurant.dto.menu;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMenuControllerRequest {
	@NotBlank
	private String name;
	private int price;
}
