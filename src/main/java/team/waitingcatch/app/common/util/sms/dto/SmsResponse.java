package team.waitingcatch.app.common.util.sms.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SmsResponse {
	private String requestId;
	private LocalDateTime requestTime;
	private String statusCode;
	private String statusName;
}