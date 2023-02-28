package team.waitingcatch.app.lineup.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import team.waitingcatch.app.lineup.dto.CreateReviewServiceRequest;
import team.waitingcatch.app.lineup.dto.GetReviewResponse;

public interface ReviewService {
	void createReview(CreateReviewServiceRequest serviceRequest) throws IOException;

	void deleteReview(Long reviewId);

	List<GetReviewResponse> getReviewsByRestaurantId(Long restaurantId);

	Slice<GetReviewResponse> getReviewsByUserId(Long id, long userId, Pageable pageable);
}