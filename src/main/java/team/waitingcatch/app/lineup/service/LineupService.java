package team.waitingcatch.app.lineup.service;

import java.util.List;

import team.waitingcatch.app.lineup.dto.CancelWaitingRequest;
import team.waitingcatch.app.lineup.dto.GetLineupRecordsServiceRequest;
import team.waitingcatch.app.lineup.dto.LineupRecordWithTypeResponse;
import team.waitingcatch.app.lineup.dto.StartWaitingServiceRequest;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;
import team.waitingcatch.app.lineup.dto.UpdateArrivalStatusServiceRequest;

public interface LineupService {
	void openLineup(Long sellerId);

	void closeLineup(Long sellerId);

	void startWaiting(StartWaitingServiceRequest serviceRequest);

	void cancelWaiting(CancelWaitingRequest request);

	List<TodayLineupResponse> getTodayLineups(Long sellerId);

	List<LineupRecordWithTypeResponse> getLineupRecords(GetLineupRecordsServiceRequest serviceRequest);

	void updateArrivalStatus(UpdateArrivalStatusServiceRequest serviceRequest);
}