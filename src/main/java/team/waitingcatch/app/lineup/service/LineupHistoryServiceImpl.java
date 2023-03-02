package team.waitingcatch.app.lineup.service;

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
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 줄서기 히스토리입니다."));
	}

	@Transactional(readOnly = true)
	@Override
	public Slice<LineupRecordResponse> _getRecordsByUserId(Long id, long userId, ArrivalStatusEnum statusCond,
		Pageable pageable) {

		return lineupHistoryRepository.findLineupRecordsByUserIdAndStatus(id, userId, statusCond, pageable);
	}

	@Override
	public void _bulkSoftDeleteByRestaurantId(Long restaurantId) {
		lineupHistoryRepository.bulkSoftDeleteByRestaurantId(restaurantId);
	}
}