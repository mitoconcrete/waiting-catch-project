package team.waitingcatch.app.lineup.repository;

import static team.waitingcatch.app.lineup.entity.QLineupHistory.*;
import static team.waitingcatch.app.restaurant.entity.QRestaurant.*;
import static team.waitingcatch.app.user.entitiy.QUser.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.dto.CustomerLineupInfoResponse;
import team.waitingcatch.app.lineup.dto.LineupRecordResponse;
import team.waitingcatch.app.lineup.dto.QCustomerLineupInfoResponse;
import team.waitingcatch.app.lineup.dto.QLineupRecordResponse;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

@RequiredArgsConstructor
public class LineupHistoryRepositoryCustomImpl implements LineupHistoryRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<LineupRecordResponse> findLineupRecordsByUserIdAndStatus(Long id, long userId,
		ArrivalStatusEnum statusCond, Pageable pageable) {
		List<LineupRecordResponse> content = queryFactory
			.select(new QLineupRecordResponse(
				lineupHistory.id,
				lineupHistory.user.id,
				lineupHistory.restaurant.id,
				restaurant.name,
				lineupHistory.numOfMembers,
				lineupHistory.status,
				lineupHistory.isReviewed,
				lineupHistory.startedAt,
				lineupHistory.arrivedAt
			))
			.from(lineupHistory)
			.join(lineupHistory.restaurant, restaurant)
			.where(
				idLt(id),
				lineupHistory.user.id.eq(userId),
				statusEq(statusCond),
				lineupHistory.isDeleted.isFalse()
			)
			.orderBy(lineupHistory.id.desc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = false;
		if (content.size() > pageable.getPageSize()) {
			content.remove(pageable.getPageSize());
			hasNext = true;
		}

		return new SliceImpl<>(content, pageable, hasNext);
	}

	@Override
	public Slice<CustomerLineupInfoResponse> findLineupHistoryForRequestReview(Long id,
		LocalDateTime localDateTime, Pageable pageable) {

		List<CustomerLineupInfoResponse> content = queryFactory
			.select(new QCustomerLineupInfoResponse(
				lineupHistory.id,
				user.name,
				user.phoneNumber,
				restaurant.id,
				restaurant.name
			))
			.from(lineupHistory)
			.join(lineupHistory.user, user)
			.join(lineupHistory.restaurant, restaurant)
			.where(
				idGt(id),
				lineupHistory.status.eq(ArrivalStatusEnum.ARRIVE),
				lineupHistory.isReviewed.isFalse(),
				lineupHistory.isReceivedReviewRequest.isFalse(),
				lineupHistory.isDeleted.isFalse(),
				lineupHistory.createdDate.gt(localDateTime)
			)
			.orderBy(lineupHistory.id.asc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = false;
		if (content.size() > pageable.getPageSize()) {
			content.remove(pageable.getPageSize());
			hasNext = true;
		}

		return new SliceImpl<>(content, pageable, hasNext);
	}


	// @Override
	// public Map<String, List<LineupHistoryStatisticsResponse>> getRestaurantLineupStatistics(long restaurantId) {
	// 	queryFactory
	// 		.select(Expressions.stringTemplate("function('date_format', {0}, {1})", lineupHistory.startedAt, "%Y-%m-%d")
	// 			.as("group"))
	// 		.from(lineupHistory)
	// 		.where(
	// 			lineupHistory.restaurant.id.eq(restaurantId),
	// 			lineupHistory.startedAt.gt(
	// 				Expressions.stringTemplate("function('date_sub', {0}, {1})", LocalDateTime.now(),
	// 					LocalDateTime.now().minusDays(7L))))
	// 		.groupBy()
	// 		.orderBy()
	// 		.fetch();
	// }

	private BooleanExpression idLt(Long id) {
		return id != null ? lineupHistory.id.lt(id) : null;
	}

	private BooleanExpression idGt(Long id) {
		return id != null ? lineupHistory.id.gt(id) : null;
	}

	private BooleanExpression statusEq(ArrivalStatusEnum statusCond) {
		return statusCond != null ? lineupHistory.status.eq(statusCond) : null;
	}
}