package team.waitingcatch.app.lineup.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import team.waitingcatch.app.user.entitiy.User;

@Getter
public class CreateReviewServiceRequest {
	private final User user;
	private final long restaurantId;
	private final int rate;
	private final String content;
	private final List<MultipartFile> images = new ArrayList<>();

	public CreateReviewServiceRequest(User user, long restaurantId, int rate, String content,
		List<MultipartFile> images) {

		this.user = user;
		this.restaurantId = restaurantId;
		this.rate = rate;
		this.content = content;
		this.images.addAll(images);
	}
}