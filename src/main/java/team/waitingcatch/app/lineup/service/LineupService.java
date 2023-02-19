package team.waitingcatch.app.lineup.service;

import java.util.List;

import team.waitingcatch.app.lineup.dto.LineupRecordWithTypeResponse;
import team.waitingcatch.app.lineup.dto.StartLineupServiceRequest;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;
import team.waitingcatch.app.lineup.dto.UpdateArrivalStatusServiceRequest;

public interface LineupService {
	void openLineup(Long sellerId);

	void closeLineup(Long sellerId);

	void startWaiting(StartLineupServiceRequest serviceRequest);

	List<TodayLineupResponse> getLineups(Long sellerId);

	List<LineupRecordWithTypeResponse> getPastLineups(Long userId);

	void updateArrivalStatus(UpdateArrivalStatusServiceRequest serviceRequest);
}