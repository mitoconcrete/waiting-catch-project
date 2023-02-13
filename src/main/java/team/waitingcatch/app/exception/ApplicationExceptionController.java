package team.waitingcatch.app.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import team.waitingcatch.app.exception.dto.BasicExceptionResponse;

@RestControllerAdvice
public class ApplicationExceptionController {
	@ExceptionHandler({IllegalArgumentException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public BasicExceptionResponse illegalArgumentExceptionHandler(IllegalArgumentException ex) {
		return new BasicExceptionResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler({DataIntegrityViolationException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public BasicExceptionResponse integrityExceptionHandler(DataIntegrityViolationException ex) {
		return new BasicExceptionResponse(HttpStatus.BAD_REQUEST, "데이터 무결성 오류 발생 : Unique 한 데이터를 넣어주어야합니다.");
	}
}
