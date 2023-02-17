package team.waitingcatch.app.lineup.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;

@Getter
public class CreateReviewEntityRequest {
	private final User user;
	private final Restaurant restaurant;
	private final int rate;
	private final String content;
	private final List<String> imagePaths = new ArrayList<>();

	public CreateReviewEntityRequest(User user, Restaurant restaurant, int rate, String content,
		List<String> imagePaths) {

		this.user = user;
		this.restaurant = restaurant;
		this.rate = rate;
		this.content = content;
		this.imagePaths.addAll(imagePaths);
	}
}