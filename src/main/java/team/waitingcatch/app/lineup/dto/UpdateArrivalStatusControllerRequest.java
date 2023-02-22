package team.waitingcatch.app.lineup.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateArrivalStatusControllerRequest {
	@Pattern(regexp = "^(CALL|CANCEL|ARRIVE)$")
	@NotNull
	private String status;
}