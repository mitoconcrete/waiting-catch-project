package team.waitingcatch.app.lineup.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import team.waitingcatch.app.lineup.enums.StoredLineupTableNameEnum;
import team.waitingcatch.app.user.entitiy.User;

@Getter
public class CreateReviewServiceRequest {
	private final StoredLineupTableNameEnum type;
	private final long lineupId;
	private final User user;
	private final long restaurantId;
	private final int rate;
	private final String content;
	private final List<MultipartFile> images = new ArrayList<>();

	public CreateReviewServiceRequest(StoredLineupTableNameEnum type, long lineupId, User user, long restaurantId,
		int rate, String content, List<MultipartFile> images) {

		this.type = type;
		this.lineupId = lineupId;
		this.user = user;
		this.restaurantId = restaurantId;
		this.rate = rate;
		this.content = content;
		if (images != null) {
			this.images.addAll(images);
		}
	}
}