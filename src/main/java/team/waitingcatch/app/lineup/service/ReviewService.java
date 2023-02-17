package team.waitingcatch.app.lineup.service;

import java.io.IOException;
import java.util.List;

import team.waitingcatch.app.lineup.dto.CreateReviewServiceRequest;
import team.waitingcatch.app.lineup.dto.GetReviewResponse;

public interface ReviewService {
	void createReview(CreateReviewServiceRequest serviceRequest) throws IOException;

	void deleteReview(Long reviewId);

	List<GetReviewResponse> getReviewsByRestaurant(Long restaurantId);
}