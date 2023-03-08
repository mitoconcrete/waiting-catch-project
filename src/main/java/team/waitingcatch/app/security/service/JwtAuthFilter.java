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
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.JwtUtil;
import team.waitingcatch.app.dto.service.UpdateTokenRequest;
import team.waitingcatch.app.exception.TokenExpiredException;
import team.waitingcatch.app.redis.dto.GetRefreshTokenRequest;
import team.waitingcatch.app.redis.dto.ValidateTokenRequest;
import team.waitingcatch.app.redis.service.AliveTokenService;
import team.waitingcatch.app.redis.service.KilledAccessTokenService;
import team.waitingcatch.app.redis.service.RemoveTokenRequest;
import team.waitingcatch.app.security.util.SecurityExceptionUtil;
import team.waitingcatch.app.user.enums.UserRoleEnum;

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
		if (!request.getRequestURI().contains("/api/general/")) {
			String token = jwtUtils.resolveToken(request);
			if (token != null) {
				try {
					// 1. token이 만료되지 않았다면, authentication principal를 등록한다.
					jwtUtils.validateToken(token);

					// 2. token 이 이미 로그아웃 내역이 있는지 확인한다.
					ValidateTokenRequest servicePayload = new ValidateTokenRequest(token);
					killedAccessTokenService.validateToken(servicePayload);

					Claims info = jwtUtils.getTokenClaims(token);
					setAuthentication(info.getSubject());
				} catch (TokenExpiredException accessTokenExpiredException) {
					// 2. 토큰이 만료되었을 경우
					try {
						// 해당 토큰이 만료되었다면 redis 내의 리프레시 토큰을 이용하여, 새로운 access 토큰을 발급해줍니다. -> 로그인 유지

						// 1. 리프레시 토큰 검증
						GetRefreshTokenRequest getServicePayload = new GetRefreshTokenRequest(token);
						String refreshToken = aliveTokenService.getRefreshToken(getServicePayload).getRefreshToken();
						jwtUtils.validateToken(refreshToken);

						// 2. 리프레시 토큰으로부터 정보들을 얻어 새로운 accessToken 생성
						Claims refreshTokenClaims = jwtUtils.getTokenClaims(refreshToken);
						String username = refreshTokenClaims.getSubject();
						String role = refreshTokenClaims.get(JwtUtil.AUTHORIZATION_KEY, String.class);
						String updateAccessToken = jwtUtils.createAccessToken(username, UserRoleEnum.valueOf(role));

						// 3. redis 의 정보 업데이트
						UpdateTokenRequest updateServicePayload = new UpdateTokenRequest(token,
							updateAccessToken.substring(7),
							refreshToken, JwtUtil.REFRESH_TOKEN_TIME);
						aliveTokenService.updateToken(updateServicePayload);

						// 4. 새로 발급받은 accesstoken 을 이용하여, context에 principal에 넣어준다.
						Claims info = jwtUtils.getTokenClaims(updateAccessToken.substring(7));
						setAuthentication(info.getSubject());

						// 5. 새로운 access Token 헤더에 넣어서 반환.
						response.setHeader(JwtUtil.AUTHORIZATION_HEADER, updateAccessToken);
					} catch (TokenExpiredException refreshTokenExpiredException) {
						// 해당 토큰이 만료되었고, redis 내에 리프레시 토큰또한 만료되었다면, 리프레시토큰을 DB에서 제거한다. -> 로그아웃
						RemoveTokenRequest removeServicePayload = new RemoveTokenRequest(token);
						aliveTokenService.removeToken(removeServicePayload);
						SecurityExceptionUtil exceptionUtil = new SecurityExceptionUtil();
						exceptionUtil.active(response, HttpStatus.UNAUTHORIZED,
							refreshTokenExpiredException.getMessage());
						return;
					} catch (RuntimeException runtimeException) {
						SecurityExceptionUtil exceptionUtil = new SecurityExceptionUtil();
						exceptionUtil.active(response, HttpStatus.UNAUTHORIZED, runtimeException.getMessage());
						return;
					}
				} catch (RuntimeException runtimeException) {
					SecurityExceptionUtil exceptionUtil = new SecurityExceptionUtil();
					exceptionUtil.active(response, HttpStatus.UNAUTHORIZED, runtimeException.getMessage());
					return;
				}
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