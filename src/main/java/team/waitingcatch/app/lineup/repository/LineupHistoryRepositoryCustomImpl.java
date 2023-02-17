package team.waitingcatch.app.lineup.repository;

import static team.waitingcatch.app.lineup.entity.QLineupHistory.*;
import static team.waitingcatch.app.restaurant.entity.QRestaurant.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.dto.PastLineupServiceResponse;
import team.waitingcatch.app.lineup.dto.QPastLineupServiceResponse;

@RequiredArgsConstructor
public class LineupHistoryRepositoryCustomImpl implements LineupHistoryRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<PastLineupServiceResponse> findAllByUserId(Long userId) {
		return queryFactory
			.select(new QPastLineupServiceResponse(
				lineupHistory.restaurant.id,
				lineupHistory.restaurant.name,
				lineupHistory.numOfMembers,
				lineupHistory.status,
				lineupHistory.isReviewed,
				lineupHistory.startedAt,
				lineupHistory.endedAt
			))
			.from(lineupHistory)
			.join(lineupHistory.restaurant, restaurant)
			.where(lineupHistory.user.id.eq(userId))
			.orderBy(lineupHistory.endedAt.desc())
			.fetch();
	}
}