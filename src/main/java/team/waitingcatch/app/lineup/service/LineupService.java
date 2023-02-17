package team.waitingcatch.app.lineup.service;

import java.util.List;

import team.waitingcatch.app.lineup.dto.StartLineupServiceRequest;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;

public interface LineupService {
	void startLineup(StartLineupServiceRequest serviceRequest);

	List<TodayLineupResponse> getLineups(Long sellerId);
}