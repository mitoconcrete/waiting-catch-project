package team.waitingcatch.app.lineup.repository;

import static team.waitingcatch.app.lineup.entity.QLineup.*;
import static team.waitingcatch.app.lineup.entity.QLineupHistory.*;
import static team.waitingcatch.app.restaurant.entity.QRestaurant.*;

import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.dto.LineupRecordResponse;
import team.waitingcatch.app.lineup.dto.QLineupRecordResponse;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

@RequiredArgsConstructor
public class LineupHistoryRepositoryCustomImpl implements LineupHistoryRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<LineupRecordResponse> findLineupRecordsByUserIdAndStatus(Long userId, ArrivalStatusEnum statusCond) {
		return queryFactory
			.select(new QLineupRecordResponse(
				lineupHistory.id,
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
			.where(lineupHistory.user.id.eq(userId), statusEq(statusCond))
			.orderBy(lineupHistory.arrivedAt.desc())
			.fetch();
	}

	private BooleanExpression statusEq(ArrivalStatusEnum statusCond) {
		return statusCond != null ? lineupHistory.status.eq(statusCond) : null;
	}
}