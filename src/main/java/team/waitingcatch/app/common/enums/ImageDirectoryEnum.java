package team.waitingcatch.app.common.enums;

import lombok.Getter;

@Getter
public enum ImageDirectoryEnum {
	RESTAURANT("restaurant"),
	MENU("menu"),
	REVIEW("review");

	private final String value;

	ImageDirectoryEnum(String value) {
		this.value = value;
	}
}