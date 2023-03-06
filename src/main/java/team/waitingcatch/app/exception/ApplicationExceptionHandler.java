package team.waitingcatch.app.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import team.waitingcatch.app.exception.dto.BasicExceptionResponse;

@RestControllerAdvice
public class ApplicationExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public BasicExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		StringBuilder builder = new StringBuilder();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			builder.append(fieldError.getDefaultMessage());
			builder.append("/");
		}
		return new BasicExceptionResponse(HttpStatus.BAD_REQUEST,
			builder.deleteCharAt(builder.lastIndexOf("/")).toString());
	}

	@ExceptionHandler({IllegalArgumentException.class, AlreadyExistsException.class, DuplicateRequestException.class,
		IllegalRequestException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public BasicExceptionResponse handleIllegalArgumentException(RuntimeException e) {
		return new BasicExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler({DataIntegrityViolationException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public BasicExceptionResponse handleDataIntegrityViolationException() {
		return new BasicExceptionResponse(HttpStatus.BAD_REQUEST,
			"데이터 무결성 오류 발생 : Unique 한 데이터를 넣어주어야합니다.");
	}

	@ExceptionHandler({HttpMessageNotReadableException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public BasicExceptionResponse httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
		return new BasicExceptionResponse(HttpStatus.BAD_REQUEST, e.getRootCause().getMessage());
	}

	@ExceptionHandler({TokenNotFoundException.class})
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public BasicExceptionResponse handleTokenNotFoundException(TokenNotFoundException e) {
		return new BasicExceptionResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
	}
}