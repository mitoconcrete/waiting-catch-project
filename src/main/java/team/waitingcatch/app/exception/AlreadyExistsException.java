package team.waitingcatch.app.exception;

public class AlreadyExistsException extends RuntimeException {
	public AlreadyExistsException(ErrorCode errorCode) {
		super(errorCode.getMessage());
	}
}