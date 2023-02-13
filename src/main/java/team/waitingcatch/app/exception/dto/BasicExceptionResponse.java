package team.waitingcatch.app.exception.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BasicExceptionResponse {
	private final HttpStatus status;
	private final String message;
}
