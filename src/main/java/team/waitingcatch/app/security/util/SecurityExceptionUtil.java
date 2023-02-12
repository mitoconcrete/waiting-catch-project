package team.waitingcatch.app.security.utils;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import team.waitingcatch.app.exception.dto.BasicExceptionResponse;

/*
Controller 이전에 에러를 처리하기 위해서는 직접 HttpServletResponse에 콘텐츠 타입과 response 값을
ObjectMapper를 이용하여 할당해주어야 한다고 합니다.
아래의 과정이 여러군데에서 반복되기 때문에, 모듈로 빼서 손쉽게 여러군데에서 가져다 쓸 수 있도록 했습니다.
*/
public class SecurityExceptionUtil {
	public void active(HttpServletResponse response, HttpStatus status, String message) throws IOException {
		// response
		BasicExceptionResponse exceptionResponse = new BasicExceptionResponse(status, message);

		// 서블릿에 필요값들 셋팅
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(exceptionResponse.getStatus().value());

		// 클라이언트에 반환
		try (OutputStream os = response.getOutputStream()) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(os, exceptionResponse);
			os.flush();
		}
	}
}
