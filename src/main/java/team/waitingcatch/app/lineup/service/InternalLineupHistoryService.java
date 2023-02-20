package team.waitingcatch.app.lineup.service;

import java.util.List;

import team.waitingcatch.app.lineup.dto.LineupRecordResponse;
import team.waitingcatch.app.lineup.entity.LineupHistory;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

public interface InternalLineupHistoryService {
	LineupHistory _getById(Long lineupId);

	List<LineupRecordResponse> _getRecordsByUserId(Long userId, ArrivalStatusEnum statusCond);
}