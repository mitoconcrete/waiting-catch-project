package team.waitingcatch.app.common.util.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
	private String to;
	private String subject;
	private String content;
}