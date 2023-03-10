package team.waitingcatch.app.lineup.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class GetReviewResponse {
	private final long restaurantId;
	private final long id;
	private final int rate;
	private final String content;
	private final List<String> imagePaths = new ArrayList<>();
	private final LocalDateTime createdDate;
	private final LocalDateTime modifiedDate;

	@QueryProjection
	public GetReviewResponse(long restaurantId, long id, int rate, String content, List<String> imagePaths,
		LocalDateTime createdDate, LocalDateTime modifiedDate) {

		this.restaurantId = restaurantId;
		this.id = id;
		this.rate = rate;
		this.content = content;
		this.imagePaths.addAll(imagePaths);
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}
}