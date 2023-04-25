package team.waitingcatch.app.lineup.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import team.waitingcatch.app.lineup.dto.CancelWaitingRequest;
import team.waitingcatch.app.lineup.dto.GetLineupHistoryRecordsServiceRequest;
import team.waitingcatch.app.lineup.dto.GetLineupPriorityServiceRequest;
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

	long getPriority(GetLineupPriorityServiceRequest serviceRequest);

	List<TodayLineupResponse> getTodayLineups(Long sellerId);

	List<LineupRecordWithTypeResponse> getLineupRecords(GetLineupRecordsServiceRequest serviceRequest);

	Slice<LineupRecordWithTypeResponse> getLineupHistoryRecords(GetLineupHistoryRecordsServiceRequest serviceRequest,
		Pageable pageable);

	void updateArrivalStatus(UpdateArrivalStatusServiceRequest serviceRequest);
}