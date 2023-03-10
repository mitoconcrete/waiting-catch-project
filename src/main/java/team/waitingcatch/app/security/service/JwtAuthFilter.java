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
					// 1. token??? ???????????? ????????????, authentication principal??? ????????????.
					jwtUtils.validateToken(token);

					// 2. token ??? ?????? ???????????? ????????? ????????? ????????????.
					ValidateTokenRequest servicePayload = new ValidateTokenRequest(token);
					killedAccessTokenService.validateToken(servicePayload);

					Claims info = jwtUtils.getTokenClaims(token);
					setAuthentication(info.getSubject());
				} catch (TokenExpiredException accessTokenExpiredException) {
					// 2. ????????? ??????????????? ??????
					try {
						// ?????? ????????? ?????????????????? redis ?????? ???????????? ????????? ????????????, ????????? access ????????? ??????????????????. -> ????????? ??????

						// 1. ???????????? ?????? ??????
						GetRefreshTokenRequest getServicePayload = new GetRefreshTokenRequest(token);
						String refreshToken = aliveTokenService.getRefreshToken(getServicePayload).getRefreshToken();
						jwtUtils.validateToken(refreshToken);

						// 2. ???????????? ?????????????????? ???????????? ?????? ????????? accessToken ??????
						Claims refreshTokenClaims = jwtUtils.getTokenClaims(refreshToken);
						String username = refreshTokenClaims.getSubject();
						String role = refreshTokenClaims.get(JwtUtil.AUTHORIZATION_KEY, String.class);
						String updateAccessToken = jwtUtils.createAccessToken(username, UserRoleEnum.valueOf(role));

						// 3. redis ??? ?????? ????????????
						UpdateTokenRequest updateServicePayload = new UpdateTokenRequest(token,
							updateAccessToken.substring(7),
							refreshToken, JwtUtil.REFRESH_TOKEN_TIME);
						aliveTokenService.updateToken(updateServicePayload);

						// 4. ?????? ???????????? accesstoken ??? ????????????, context??? principal??? ????????????.
						Claims info = jwtUtils.getTokenClaims(updateAccessToken.substring(7));
						setAuthentication(info.getSubject());

						// 5. ????????? access Token ????????? ????????? ??????.
						response.setHeader(JwtUtil.AUTHORIZATION_HEADER, updateAccessToken);
					} catch (TokenExpiredException refreshTokenExpiredException) {
						// ?????? ????????? ???????????????, redis ?????? ???????????? ???????????? ??????????????????, ????????????????????? DB?????? ????????????. -> ????????????
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