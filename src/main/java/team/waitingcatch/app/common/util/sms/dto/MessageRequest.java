package team.waitingcatch.app.common.util.sms.dto;

import lombok.Getter;

@Getter
public class MessageRequest {
	private final String to;
	private final String subject;
	private final String content;

	public MessageRequest(String to, String subject, String content) {
		this.to = to.replaceAll("-", "");
		this.subject = subject;
		this.content = content;
	}
}