package team.waitingcatch.app.lineup.repository;

import static team.waitingcatch.app.lineup.entity.QReview.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.dto.GetReviewResponse;
import team.waitingcatch.app.lineup.dto.QGetReviewResponse;

@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<GetReviewResponse> findAllByRestaurantId(Long restaurantId) {
		return queryFactory
			.select(new QGetReviewResponse(
				review.rate,
				review.content,
				review.imagePaths,
				review.createdDate,
				review.modifiedDate
			))
			.from(review)
			.where(review.restaurant.id.eq(restaurantId))
			.fetch();
	}
}