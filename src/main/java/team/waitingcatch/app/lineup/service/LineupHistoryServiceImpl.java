package team.waitingcatch.app.lineup.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.dto.LineupRecordResponse;
import team.waitingcatch.app.lineup.repository.LineupHistoryRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineupHistoryServiceImpl implements LineupHistoryService, InternalLineupHistoryService {
	private final LineupHistoryRepository lineupHistoryRepository;

	@Override
	public List<LineupRecordResponse> _getAllByUserId(Long userId) {
		return lineupHistoryRepository.findAllByUserId(userId);
	}
}