package team.waitingcatch.app.exception;

import lombok.Getter;
import team.waitingcatch.app.lineup.entity.Lineup;
import team.waitingcatch.app.lineup.service.LineupServiceImpl;

@Getter
public enum ErrorCode {
	NOT_FOUND_USER("사용자를 찾을 수 없습니다."),
	ALREADY_EXISTS_USERNAME("이미 해당 아이디가 존재합니다."),
	INCORRECT_PASSWORD("패스워드가 일치하지 않습니다."),

	NOT_FOUND_CATEGORY("존재하지 않는 카테고리입니다."),

	NOT_FOUND_SELLER_REQUEST("존재하지 않는 판매자 신청 정보입니다."),

	NOT_FOUND_BLACKLIST_DEMAND("존재하지 않는 블랙리스트 요청입니다."),
	NOT_FOUND_BLACKLIST("존재하지 않는 블랙리스트입니다."),
	ALREADY_DELETED_BLACKLIST("이미 블랙리스트에서 삭제된 유저입니다."),
	ALREADY_BANNED_USER("이미 차단된 사용자 입니다"),

	NOT_FOUND_RESTAURANT("존재하지 않는 레스토랑입니다"),
	NOT_FOUND_RESTAURANT_INFO("존재하지 않는 레스토랑 정보입니다."),
	NOT_IN_RESTAURANT("현재 레스토랑의 손님이 아닙니다."),

	CLOSED_LINEUP("줄서기가 마감되었습니다."),

	NOT_FOUND_LINEUP("존재하지 않는 줄서기입니다."),
	NOT_FOUND_LINEUP_HISTORY("존재하지 않는 줄서기 히스토리입니다."),

	LINEUP_ALEADY_CANCELED("이미 취소된 줄서기입니다."),
	LINEUP_ALEADY_ARRIVED("이미 완료된 줄서기입니다."),

	EXCEED_MAX_LINEUP_COUNT("줄서기는 동시에 최대 " + LineupServiceImpl.MAX_LINEUP_COUNT + "곳만 가능합니다."),
	EXCEED_MAX_CALL_COUNT("호출은 최대" + Lineup.MAX_CALL_COUNT + "번까지 가능합니다."),

	NOT_FOUND_WAITING_NUMBER("존재하지 않는 대기 번호입니다."),
	DISTANCE_EXCEEDED(LineupServiceImpl.MAX_DISTANCE + "km 이내의 레스토랑에만 줄서기가 가능합니다."),

	NOT_FOUND_EVENT("존재하지 않는 이벤트입니다."),
	NOT_FOUND_COUPON_CRETOR("존재하지 않는 쿠폰 생성자입니다."),
	DUPLICATE_COUPON("이미 발급받은 쿠폰입니다."),

	CONNCURRENT_REQUEST_FAILURE("이용자가 많아 요청을 처리할 수 없습니다. 잠시 후 다시 시도해 주세요."),

	ALREADY_REJECTED("이미 거절되었습니다. 다시 요청해 주세요."),
	ALREADY_APPROVED("이미 승인되었습니다."),
	ALREADY_CANCELED("이미 취소하셨습니다."),

	NOT_FOUND_TOKEN("토큰을 찾을 수 없습니다"),

	ILLEGAL_ACCESS("잘못된 접근입니다"),
	INTERNAL_ERROR("내부 오류");

	private final String message;

	ErrorCode(String message) {
		this.message = message;
	}
}