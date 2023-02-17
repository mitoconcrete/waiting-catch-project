package team.waitingcatch.app.lineup.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateReviewControllerRequest {
	@Range(min = 1, max = 5)
	private int rate;

	@NotBlank
	@Size(min = 10, max = 500)
	private String content;
}