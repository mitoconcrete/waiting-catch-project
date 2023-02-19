package team.waitingcatch.app.lineup.repository;

import java.util.List;

import team.waitingcatch.app.lineup.dto.CallCustomerInfoResponse;
import team.waitingcatch.app.lineup.dto.LineupRecordResponse;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;

public interface LineupRepositoryCustom {
	List<LineupRecordResponse> findAllRecordByUserId(Long userId);

	List<TodayLineupResponse> findAllTodayBySellerId(Long sellerId);

	CallCustomerInfoResponse findCallCustomerInfoById(Long lineupId);
}