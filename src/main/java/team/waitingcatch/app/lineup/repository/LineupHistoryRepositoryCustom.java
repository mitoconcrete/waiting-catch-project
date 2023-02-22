package team.waitingcatch.app.lineup.repository;

import java.util.List;

import team.waitingcatch.app.lineup.dto.LineupRecordResponse;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

public interface LineupHistoryRepositoryCustom {
	List<LineupRecordResponse> findLineupRecordsByUserIdAndStatus(Long userId, ArrivalStatusEnum statusCond);
}