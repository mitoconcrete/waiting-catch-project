package team.waitingcatch.app.lineup.repository;

import java.util.List;

import team.waitingcatch.app.lineup.dto.GetReviewResponse;

public interface ReviewRepositoryCustom {
	List<GetReviewResponse> findAllByRestaurantId(Long restaurantId);
}