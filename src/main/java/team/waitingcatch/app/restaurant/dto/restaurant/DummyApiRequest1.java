package team.waitingcatch.app.restaurant.dto.restaurant;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DummyApiRequest1 {
	private List<String> x;
	private List<String> y;

	public DummyApiRequest1(List<String> x, List<String> y) {
		this.x = x;
		this.y = y;
	}
}
