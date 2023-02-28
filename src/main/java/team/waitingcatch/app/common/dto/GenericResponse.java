package team.waitingcatch.app.common.dto;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GenericResponse<T> {
	private final T data;
}