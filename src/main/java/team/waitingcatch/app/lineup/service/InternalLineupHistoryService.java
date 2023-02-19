package team.waitingcatch.app.lineup.service;

import java.util.List;

import team.waitingcatch.app.lineup.dto.LineupRecordResponse;
import team.waitingcatch.app.lineup.entity.LineupHistory;

public interface InternalLineupHistoryService {
	LineupHistory _getById(Long lineupId);

	List<LineupRecordResponse> _getAllRecordByUserId(Long userId);
}