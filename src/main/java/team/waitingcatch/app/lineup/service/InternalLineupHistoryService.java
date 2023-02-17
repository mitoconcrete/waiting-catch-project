package team.waitingcatch.app.lineup.service;

import java.util.List;

import team.waitingcatch.app.lineup.dto.PastLineupResponse;

public interface InternalLineupHistoryService {
	List<PastLineupResponse> _getAllByUserId(Long userId);
}