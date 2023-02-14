package team.waitingcatch.app.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import team.waitingcatch.app.security.util.SecurityExceptionUtil;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {
		SecurityExceptionUtil exceptionUtil = new SecurityExceptionUtil();
		exceptionUtil.active(response, HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.getReasonPhrase());
	}
}
