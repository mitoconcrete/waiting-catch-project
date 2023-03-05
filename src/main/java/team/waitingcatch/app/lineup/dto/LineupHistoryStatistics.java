package team.waitingcatch.app.lineup.dto;

public interface LineupHistoryStatistics {
	String getDate();

	int getWaitingNumber();

	int avgWaitingTime();
}