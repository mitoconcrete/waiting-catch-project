package team.waitingcatch.app.exception;

import static team.waitingcatch.app.exception.ErrorCode.*;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import team.waitingcatch.app.exception.dto.BasicExceptionResponse;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public BasicExceptionResponse handleBindException(BindException e) {
		BindingResult bindingResult = e.getBindingResult();
		StringBuilder stringBuilder = new StringBuilder();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			stringBuilder.append(fieldError.getDefaultMessage());
		}
		return new BasicExceptionResponse(HttpStatus.BAD_REQUEST, stringBuilder.toString());
	}

	@ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class, AlreadyExistsException.class,
		DuplicateRequestException.class, IllegalRequestException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public BasicExceptionResponse handleIllegalArgumentException(RuntimeException e) {
		return new BasicExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler({DataIntegrityViolationException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public BasicExceptionResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		return new BasicExceptionResponse(HttpStatus.BAD_REQUEST, e.getRootCause().getMessage());
	}

	@ExceptionHandler({HttpMessageNotReadableException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public BasicExceptionResponse httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
		return new BasicExceptionResponse(HttpStatus.BAD_REQUEST, e.getRootCause().getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public BasicExceptionResponse handleInternalServerError(Exception e) {
		log.error(e.getMessage());
		return new BasicExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR.getMessage());
	}
}