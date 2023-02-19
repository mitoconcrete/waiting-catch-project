package team.waitingcatch.app.lineup.repository;

import java.util.List;

import team.waitingcatch.app.lineup.dto.LineupRecordResponse;

public interface LineupHistoryRepositoryCustom {
	List<LineupRecordResponse> findAllByUserId(Long userId);
}