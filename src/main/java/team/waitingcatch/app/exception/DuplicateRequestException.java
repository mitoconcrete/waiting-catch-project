package team.waitingcatch.app.exception;

public class DuplicateRequestException extends RuntimeException {
	public DuplicateRequestException(ErrorCode errorCode) {
		super(errorCode.getMessage());
	}
}