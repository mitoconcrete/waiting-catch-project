package team.waitingcatch.app.restaurant.enums;

import lombok.Getter;

@Getter
public enum AcceptedStatusEnum {
	WAIT("대기"),
	REJECT("거절"),
	APPROVE("승인"),
	CANCEL("취소");

	private final String value;

	AcceptedStatusEnum(String value) {
		this.value = value;
	}
}