package team.waitingcatch.app.lineup.repository;

import static com.querydsl.jpa.JPAExpressions.*;
import static team.waitingcatch.app.lineup.entity.QLineup.*;
import static team.waitingcatch.app.restaurant.entity.QRestaurant.*;
import static team.waitingcatch.app.user.entitiy.QUser.*;

import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.dto.CallCustomerInfoResponse;
import team.waitingcatch.app.lineup.dto.CustomerLineupInfoResponse;
import team.waitingcatch.app.lineup.dto.LineupRecordResponse;
import team.waitingcatch.app.lineup.dto.QCallCustomerInfoResponse;
import team.waitingcatch.app.lineup.dto.QCustomerLineupInfoResponse;
import team.waitingcatch.app.lineup.dto.QLineupRecordResponse;
import team.waitingcatch.app.lineup.dto.QTodayLineupResponse;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

@RequiredArgsConstructor
public class LineupRepositoryCustomImpl implements LineupRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<LineupRecordResponse> findRecordsByUserIdAndStatus(Long userId, ArrivalStatusEnum statusCond) {
		return queryFactory
			.select(new QLineupRecordResponse(
				lineup.id,
				lineup.user.id,
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
			.where(lineup.user.id.eq(userId), statusEq(statusCond), lineup.isDeleted.isFalse())
			.orderBy(lineup.id.desc())
			.fetch();
	}

	@Override
	public List<TodayLineupResponse> findTodayLineupsBySellerId(Long sellerId) {
		return queryFactory
			.select(new QTodayLineupResponse(
				lineup.id,
				lineup.user.id,
				lineup.waitingNumber,
				user.name,
				lineup.numOfMembers,
				lineup.status,
				lineup.callCount,
				lineup.createdDate,
				lineup.arrivedAt))
			.from(lineup)
			.join(lineup.user, user)
			.where(lineup.restaurant.id.eq(
				select(restaurant.id)
					.from(restaurant)
					.where(restaurant.user.id.eq(sellerId))
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
			.where(lineup.id.eq(lineupId), lineup.isDeleted.isFalse())
			.fetchOne();
	}

	@Override
	public List<CustomerLineupInfoResponse> findCustomerLineupInfoByIsReviewedFalse() {
		return queryFactory
			.select(new QCustomerLineupInfoResponse(
				user.name,
				user.phoneNumber,
				restaurant.name
			))
			.from(lineup)
			.join(lineup.user, user)
			.join(lineup.restaurant, restaurant)
			.where(lineup.isReviewed.isFalse(), lineup.isDeleted.isFalse())
			.fetch();
	}

	private BooleanExpression statusEq(ArrivalStatusEnum statusCond) {
		return statusCond != null ? lineup.status.eq(statusCond) : null;
	}
}