package team.waitingcatch.app.lineup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.lineup.entity.LineupHistory;

public interface LineupHistoryRepository extends JpaRepository<LineupHistory, Long> {
}