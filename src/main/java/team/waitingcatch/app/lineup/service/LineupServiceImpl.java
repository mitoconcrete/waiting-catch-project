package team.waitingcatch.app.lineup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.repository.LineupHistoryRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class LineupServiceImpl implements LineupService, InternalLineupService {

	private final LineupHistoryRepository lineupHistoryRepository;

}