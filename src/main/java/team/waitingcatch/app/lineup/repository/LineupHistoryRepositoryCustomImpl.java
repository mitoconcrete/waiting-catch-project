package team.waitingcatch.app.lineup.repository;

import static team.waitingcatch.app.lineup.entity.QLineupHistory.*;
import static team.waitingcatch.app.restaurant.entity.QRestaurant.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

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
	public Slice<LineupRecordResponse> findLineupRecordsByUserIdAndStatus(Long id, long userId,
		ArrivalStatusEnum statusCond, Pageable pageable) {

		List<LineupRecordResponse> content = queryFactory
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
			.where(idLt(id), lineupHistory.user.id.eq(userId), statusEq(statusCond))
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

	private BooleanExpression idLt(Long id) {
		return id != null ? lineupHistory.id.lt(id) : null;
	}

	private BooleanExpression statusEq(ArrivalStatusEnum statusCond) {
		return statusCond != null ? lineupHistory.status.eq(statusCond) : null;
	}
}