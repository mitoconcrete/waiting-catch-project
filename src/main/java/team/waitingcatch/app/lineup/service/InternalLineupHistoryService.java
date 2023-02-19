package team.waitingcatch.app.lineup.service;

import java.util.List;

import team.waitingcatch.app.lineup.dto.LineupRecordResponse;

public interface InternalLineupHistoryService {
	List<LineupRecordResponse> _getAllByUserId(Long userId);
}