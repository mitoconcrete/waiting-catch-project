package team.waitingcatch.app.lineup.dto;

import org.hibernate.validator.constraints.Range;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StartLineupControllerRequest {
	@Range(min = 1, max = 10)
	private int numOfMember;
}