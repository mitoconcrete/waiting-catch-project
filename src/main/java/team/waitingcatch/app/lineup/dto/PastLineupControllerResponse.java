package team.waitingcatch.app.lineup.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class PastLineupControllerResponse {
	private final String username;
	private final List<PastLineupServiceResponse> pastLineup;

	public PastLineupControllerResponse(String username, List<PastLineupServiceResponse> pastLineup) {
		this.username = username;
		this.pastLineup = new ArrayList<>(pastLineup);
	}
}