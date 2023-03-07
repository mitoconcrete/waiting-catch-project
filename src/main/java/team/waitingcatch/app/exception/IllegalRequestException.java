package team.waitingcatch.app.exception;

public class IllegalRequestException extends RuntimeException {
	public IllegalRequestException(ErrorCode errorCode) {
		super(errorCode.getMessage());
	}
}