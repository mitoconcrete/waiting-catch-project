package team.waitingcatch.app.lineup.service;

import team.waitingcatch.app.lineup.entity.Lineup;

public interface InternalLineupService {
	Lineup _getLineupById(Long id);
}