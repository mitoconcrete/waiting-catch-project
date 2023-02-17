package team.waitingcatch.app.lineup.dto;

import javax.validation.constraints.Pattern;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateArrivalStatusControllerRequest {
	@Pattern(regexp = "^(CALL|CANCEL|ARRIVE)$")
	private String status;
}