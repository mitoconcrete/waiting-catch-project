package team.waitingcatch.app.lineup.repository;

import java.util.List;

import team.waitingcatch.app.lineup.dto.PastLineupResponse;

public interface LineupHistoryRepositoryCustom {
	List<PastLineupResponse> findAllByUserId(Long userId);
}