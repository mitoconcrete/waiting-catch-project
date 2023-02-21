package team.waitingcatch.app.lineup.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateReviewControllerRequest {
	@Pattern(regexp = "^(LINEUP|LINEUP_HISTORY)$")
	@NotNull
	private String type;

	private long lineupId;

	@Range(min = 0, max = 5)
	private int rate;

	@Size(min = 10, max = 500)
	@NotBlank
	private String content;
}