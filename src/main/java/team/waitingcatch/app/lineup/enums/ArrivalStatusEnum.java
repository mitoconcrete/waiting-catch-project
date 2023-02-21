package team.waitingcatch.app.lineup.enums;

import lombok.Getter;

@Getter
public enum ArrivalStatusEnum {
	WAIT("대기"),
	CALL("호출"),
	CANCEL("취소"),
	ARRIVE("도착");

	private final String value;

	ArrivalStatusEnum(String value) {
		this.value = value;
	}
}