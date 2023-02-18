package team.waitingcatch.app.restaurant.dto.restaurant;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SearchRestaurantServiceRequest {
	@NotBlank(message = "키워드를 입력해 주세요.")
	private final String keyword;
	private final double latitude;
	private final double longitude;
}
