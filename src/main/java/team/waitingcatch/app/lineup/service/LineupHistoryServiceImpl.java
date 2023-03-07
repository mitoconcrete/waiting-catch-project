package team.waitingcatch.app.lineup.service;

import static team.waitingcatch.app.exception.ErrorCode.*;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.dto.LineupRecordResponse;
import team.waitingcatch.app.lineup.entity.LineupHistory;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;
import team.waitingcatch.app.lineup.repository.LineupHistoryRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineupHistoryServiceImpl implements LineupHistoryService, InternalLineupHistoryService {
	private final LineupHistoryRepository lineupHistoryRepository;

	@Transactional(readOnly = true)
	@Override
	public LineupHistory _getById(Long id) {
		return lineupHistoryRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_LINEUP_HISTORY.getMessage()));
	}

	@Transactional(readOnly = true)
	@Override
	public Slice<LineupRecordResponse> _getRecordsByUserId(Long id, long userId, ArrivalStatusEnum statusCond,
		Pageable pageable) {
		System.out.println(id + "??");
		return lineupHistoryRepository.findLineupRecordsByUserIdAndStatus(id, userId, statusCond, pageable);
	}

	@Override
	public void _bulkSoftDeleteByRestaurantId(Long restaurantId) {
		lineupHistoryRepository.bulkSoftDeleteByRestaurantId(restaurantId);
	}
}