package team.waitingcatch.app.lineup.repository;

import java.util.List;

import team.waitingcatch.app.lineup.dto.PastLineupServiceResponse;

public interface LineupHistoryRepositoryCustom {
	List<PastLineupServiceResponse> findAllByUserId(Long userId);
}