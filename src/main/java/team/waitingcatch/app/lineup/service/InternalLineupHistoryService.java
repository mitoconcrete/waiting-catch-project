package team.waitingcatch.app.lineup.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import team.waitingcatch.app.lineup.dto.LineupRecordResponse;
import team.waitingcatch.app.lineup.entity.LineupHistory;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

public interface InternalLineupHistoryService {
	LineupHistory _getById(Long lineupId);

	Slice<LineupRecordResponse> _getRecordsByUserId(Long id, long userId, ArrivalStatusEnum statusCond, Pageable pageable);

	void _bulkSoftDeleteByRestaurantId(Long restaurantId);
}