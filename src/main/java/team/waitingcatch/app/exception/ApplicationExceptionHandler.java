package team.waitingcatch.app.exception;

import static team.waitingcatch.app.exception.ErrorCode.*;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import team.waitingcatch.app.exception.dto.BasicExceptionResponse;

@RestControllerAdvice
@Slf4j
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
		IllegalRequestException.class, NoSuchElementException.class})
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

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public BasicExceptionResponse handleTokenNotFoundException(Exception e) {
		for (StackTraceElement element : e.getStackTrace()) {
			log.error(element.toString());
		}
		return new BasicExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR.getMessage());
	}
}