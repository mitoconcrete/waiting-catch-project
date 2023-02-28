package team.waitingcatch.app.lineup.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import team.waitingcatch.app.lineup.dto.GetReviewResponse;

public interface ReviewRepositoryCustom {
	List<GetReviewResponse> findAllByRestaurantId(Long restaurantId);

	Slice<GetReviewResponse> findAllByUserId(Long id, long userId, Pageable pageable);
}