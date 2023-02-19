package team.waitingcatch.app.lineup.service;

import java.util.List;

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
	public List<LineupRecordResponse> _getRecordsByUserId(Long userId, ArrivalStatusEnum statusCond) {
		return lineupHistoryRepository.findLineupRecordsByUserIdAndStatus(userId, statusCond);
	}
}