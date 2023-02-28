package team.waitingcatch.app.lineup.repository;

import static team.waitingcatch.app.lineup.entity.QReview.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.dto.GetReviewResponse;
import team.waitingcatch.app.lineup.dto.QGetReviewResponse;

@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<GetReviewResponse> findAllByRestaurantId(Long id, long restaurantId, Pageable pageable) {
		List<GetReviewResponse> content = queryFactory
			.select(new QGetReviewResponse(
				review.id,
				review.rate,
				review.content,
				review.imagePaths,
				review.createdDate,
				review.modifiedDate
			))
			.from(review)
			.where(idLt(id), review.restaurant.id.eq(restaurantId).and(review.isDeleted.isFalse()))
			.orderBy(review.id.desc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = hasNext(content, pageable);

		return new SliceImpl<>(content, pageable, hasNext);
	}

	@Override
	public Slice<GetReviewResponse> findAllByUserId(Long id, long userId, Pageable pageable) {
		List<GetReviewResponse> content = queryFactory
			.select(new QGetReviewResponse(
				review.id,
				review.rate,
				review.content,
				review.imagePaths,
				review.createdDate,
				review.modifiedDate
			))
			.from(review)
			.where(idLt(id), review.user.id.eq(userId).and(review.isDeleted.isFalse()))
			.orderBy(review.id.desc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = hasNext(content, pageable);

		return new SliceImpl<>(content, pageable, hasNext);
	}

	private BooleanExpression idLt(Long id) {
		return id != null ? review.id.lt(id) : null;
	}

	private boolean hasNext(List<GetReviewResponse> content, Pageable pageable) {
		boolean hasNext = false;
		if (content.size() > pageable.getPageSize()) {
			content.remove(pageable.getPageSize());
			hasNext = true;
		}
		return hasNext;
	}
}