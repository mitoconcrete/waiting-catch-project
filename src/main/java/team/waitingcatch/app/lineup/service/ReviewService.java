package team.waitingcatch.app.lineup.service;

import java.io.IOException;

import team.waitingcatch.app.lineup.dto.CreateReviewServiceRequest;

public interface ReviewService {
	void createReview(CreateReviewServiceRequest serviceRequest) throws IOException;

	void deleteReview(Long reviewId);
}