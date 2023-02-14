package team.waitingcatch.app.security.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.JwtUtil;
import team.waitingcatch.app.dto.service.UpdateTokenRequest;
import team.waitingcatch.app.redis.dto.GetRefreshTokenRequest;
import team.waitingcatch.app.redis.dto.ValidateTokenRequest;
import team.waitingcatch.app.redis.service.AliveTokenService;
import team.waitingcatch.app.redis.service.KilledAccessTokenService;
import team.waitingcatch.app.redis.service.RemoveTokenRequest;
import team.waitingcatch.app.security.util.SecurityExceptionUtil;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtils;
	private final UserDetailsService userDetailsService;
	private final AliveTokenService aliveTokenService;
	private final KilledAccessTokenService killedAccessTokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain)
		throws ServletException, IOException {

		String token = jwtUtils.resolveToken(request);

		if (token != null) {
			try {
				// 1. token이 만료되지 않았다면, authentication principal를 등록한다.
				jwtUtils.validateToken(token);

				// 2. token 이 이미 로그아웃 내역이 있는지 확인한다.
				ValidateTokenRequest servicePayload = new ValidateTokenRequest(token);
				killedAccessTokenService.validateToken(servicePayload);

				Claims info = jwtUtils.getUserInfoFromToken(token);
				setAuthentication(info.getSubject());
			} catch (ExpiredJwtException accessTokenExpiredException) {
				// 2. 토큰이 만료되었을 경우
				try {
					// 해당 토큰이 만료되었다면 redis 내의 리프레시 토큰을 이용하여, 새로운 access 토큰을 발급해줍니다. -> 로그인 유지
					GetRefreshTokenRequest getServicePayload = new GetRefreshTokenRequest(token);
					String refreshToken = aliveTokenService.getRefreshToken(getServicePayload).getRefreshToken();
					jwtUtils.validateToken(refreshToken);

					UpdateTokenRequest updateServicePayload = new UpdateTokenRequest(token, refreshToken);
					aliveTokenService.updateToken(updateServicePayload);
				} catch (ExpiredJwtException refreshTokenExpiredException) {
					// 해당 토큰이 만료되었고, redis 내에 리프레시 토큰또한 만료되었다면, 리프레시토큰을 DB에서 제거한다. -> 로그아웃
					RemoveTokenRequest removeServicePayload = new RemoveTokenRequest(token);
					aliveTokenService.removeToken(removeServicePayload);
				} catch (RuntimeException runtimeException) {
					SecurityExceptionUtil exceptionUtil = new SecurityExceptionUtil();
					exceptionUtil.active(response, HttpStatus.UNAUTHORIZED, runtimeException.getMessage());
				}
				return;
			} catch (RuntimeException runtimeException) {
				SecurityExceptionUtil exceptionUtil = new SecurityExceptionUtil();
				exceptionUtil.active(response, HttpStatus.UNAUTHORIZED, runtimeException.getMessage());
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private void setAuthentication(String username) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = createAuthentication(username);
		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);
	}

	public Authentication createAuthentication(String username) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}