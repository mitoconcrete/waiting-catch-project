package team.waitingcatch.app.lineup.repository;

import static team.waitingcatch.app.lineup.entity.QLineup.*;
import static team.waitingcatch.app.restaurant.entity.QRestaurant.*;
import static team.waitingcatch.app.user.entitiy.QUser.*;

import java.util.List;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.dto.QTodayLineupResponse;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;

@RequiredArgsConstructor
public class LineupRepositoryCustomImpl implements LineupRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<TodayLineupResponse> findAllBySellerId(Long sellerId) {
		return queryFactory
			.select(new QTodayLineupResponse(
				lineup.waitingNumber,
				lineup.numOfMembers,
				lineup.status,
				lineup.callCount,
				lineup.startedAt,
				lineup.endedAt))
			.from(lineup)
			.where(lineup.restaurant.id.eq(
				JPAExpressions
					.select(restaurant.id)
					.from(restaurant)
					.where(user.id.eq(sellerId))
			))
			.fetch();
	}
}