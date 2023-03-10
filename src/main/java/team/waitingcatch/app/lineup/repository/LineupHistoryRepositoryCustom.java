package team.waitingcatch.app.lineup.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import team.waitingcatch.app.lineup.dto.CustomerLineupInfoResponse;
import team.waitingcatch.app.lineup.dto.LineupRecordResponse;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;

public interface LineupHistoryRepositoryCustom {
	Slice<LineupRecordResponse> findLineupRecordsByUserIdAndStatus(Long id, long userId, ArrivalStatusEnum statusCond,
		Pageable pageable);

	Slice<CustomerLineupInfoResponse> findLineupHistoryForRequestReview(Long id, LocalDateTime localDateTime,
		Pageable pageable);
}