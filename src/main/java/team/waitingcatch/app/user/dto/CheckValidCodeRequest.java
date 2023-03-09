package team.waitingcatch.app.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckValidCodeRequest {
	private String phoneNumber;
	private int validCode;
}
