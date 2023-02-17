package team.waitingcatch.app.lineup.repository;

import java.util.List;

import team.waitingcatch.app.lineup.dto.PastLineupResponse;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;

public interface LineupRepositoryCustom {
	List<PastLineupResponse> findAllByUserId(Long userId);

	List<TodayLineupResponse> findAllBySellerId(Long sellerId);
}