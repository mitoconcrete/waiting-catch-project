package team.waitingcatch.app.lineup.repository;

import static team.waitingcatch.app.lineup.entity.QLineup.*;
import static team.waitingcatch.app.restaurant.entity.QRestaurant.*;
import static team.waitingcatch.app.user.entitiy.QUser.*;

import java.util.List;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.dto.CallCustomerInfoResponse;
import team.waitingcatch.app.lineup.dto.LineupRecordResponse;
import team.waitingcatch.app.lineup.dto.QCallCustomerInfoResponse;
import team.waitingcatch.app.lineup.dto.QLineupRecordResponse;
import team.waitingcatch.app.lineup.dto.QTodayLineupResponse;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;

@RequiredArgsConstructor
public class LineupRepositoryCustomImpl implements LineupRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	@Override
	public List<LineupRecordResponse> findAllByUserId(Long userId) {
		return queryFactory
			.select(new QLineupRecordResponse(
				lineup.id,
				lineup.restaurant.id,
				restaurant.name,
				lineup.numOfMembers,
				lineup.status,
				lineup.isReviewed,
				lineup.createdDate,
				lineup.arrivedAt
			))
			.from(lineup)
			.join(lineup.restaurant, restaurant)
			.where(lineup.user.id.eq(userId))
			.orderBy(lineup.arrivedAt.desc())
			.fetch();
	}

	@Override
	public List<TodayLineupResponse> findAllBySellerId(Long sellerId) {
		return queryFactory
			.select(new QTodayLineupResponse(
				lineup.waitingNumber,
				lineup.numOfMembers,
				lineup.status,
				lineup.callCount,
				lineup.createdDate,
				lineup.arrivedAt))
			.from(lineup)
			.where(lineup.restaurant.id.eq(
				JPAExpressions
					.select(restaurant.id)
					.from(restaurant)
					.where(user.id.eq(sellerId))
			))
			.orderBy(lineup.waitingNumber.asc())
			.fetch();
	}

	@Override
	public CallCustomerInfoResponse findCallCustomerInfoById(Long lineupId) {
		return queryFactory
			.select(new QCallCustomerInfoResponse(
				user.phoneNumber,
				restaurant.name,
				lineup.waitingNumber))
			.from(lineup)
			.join(lineup.user, user)
			.join(lineup.restaurant, restaurant)
			.where(lineup.id.eq(lineupId))
			.fetchOne();
	}
}