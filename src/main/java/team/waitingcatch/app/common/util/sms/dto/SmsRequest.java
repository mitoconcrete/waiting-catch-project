package team.waitingcatch.app.common.util.sms.dto;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SmsRequest {
	private final String type;
	private final String contentType;
	private final String countryCode;
	private final String from;
	private final String subject;
	private final String content;
	private final List<MessageRequest> messages;
}