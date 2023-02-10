package team.waitingcatch.app.lineup.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.repository.LineupHistoryRepository;

@Service
@RequiredArgsConstructor
public class LineupServiceImpl implements LineupService, InternalLineupService {

	private final LineupHistoryRepository lineupHistoryRepository;

}