package team.waitingcatch.app.restaurant.dto.restaurant;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateRestaurantControllerRequest {
	@NotNull
	@Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "올바른 전화번호 형식을 입력하세요.")
	private String phoneNumber;

	@Min(1)
	private int capacity;

	@NotNull
	@Size(min = 10, max = 1000)
	private String description;

	@NotNull
	@Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")
	private String openTime;

	@NotNull
	@Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")
	private String closeTime;
}