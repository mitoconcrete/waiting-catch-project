package team.waitingcatch.app.lineup.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import team.waitingcatch.app.lineup.dto.CallCustomerInfoResponse;
import team.waitingcatch.app.lineup.dto.CustomerLineupInfoResponse;
import team.waitingcatch.app.lineup.dto.LineupRecordResponse;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;
import team.waitingcatch.app.lineup.entity.Lineup;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

public interface LineupRepositoryCustom {
	List<LineupRecordResponse> findRecordsByUserIdAndStatus(Long userId, ArrivalStatusEnum statusCond);

	List<TodayLineupResponse> findTodayLineupsBySellerId(Long sellerId);

	CallCustomerInfoResponse findCallCustomerInfoById(Long lineupId);

	Slice<CustomerLineupInfoResponse> findCustomerLineupInfoByIsReviewedFalse(Pageable pageable);

	Slice<Lineup> findAll(Long id, Pageable pageable);
}