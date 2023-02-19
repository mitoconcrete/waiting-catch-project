package team.waitingcatch.app.lineup.repository;

import java.util.List;

import team.waitingcatch.app.lineup.dto.CallCustomerInfoResponse;
import team.waitingcatch.app.lineup.dto.CustomerLineupInfoResponse;
import team.waitingcatch.app.lineup.dto.LineupRecordResponse;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

public interface LineupRepositoryCustom {
	List<LineupRecordResponse> findRecordsByUserIdAndStatus(Long userId, ArrivalStatusEnum statusCond);

	List<TodayLineupResponse> findTodayLineupsBySellerId(Long sellerId);

	CallCustomerInfoResponse findCallCustomerInfoById(Long lineupId);

	List<CustomerLineupInfoResponse> findCustomerLineupInfoByIsReviewedFalse();
}