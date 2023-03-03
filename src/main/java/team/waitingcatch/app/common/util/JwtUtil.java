package team.waitingcatch.app.common.util;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.exception.TokenExpiredException;
import team.waitingcatch.app.user.enums.UserRoleEnum;

@Component
@RequiredArgsConstructor
public class JwtUtil {
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String AUTHORIZATION_KEY = "auth";
	public static final String BEARER_PREFIX = "Bearer ";

	public static final long ACCESS_TOKEN_TIME = 1000 * 60 * 30L;
	public static final long REFRESH_TOKEN_TIME = 1000 * 60 * 60 * 24 * 14L;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;

	@PostConstruct// 객체 생성시 초기화 해주는 기능
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	public String createAccessToken(String username, UserRoleEnum role) {
		return createToken(username, role, ACCESS_TOKEN_TIME);
	}

	public String createRefreshToken(String username, UserRoleEnum role) {
		return createToken(username, role, REFRESH_TOKEN_TIME);
	}

	// 토큰 생성
	public String createToken(String username, UserRoleEnum role, long token_time) {
		Date date = new Date();
		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(username) // 토큰 정보 안에 username을 넣어줌
				.claim(AUTHORIZATION_KEY, role) // 권한 가져오기
				.setExpiration(new Date(date.getTime() + token_time))  // 들어오는 만료 시간에 따라 리프레시, 엑세스 토큰 구분 지으려고 함.
				.setIssuedAt(date) // 토큰 생성 시점
				.signWith(key, signatureAlgorithm) // secret key를 이용해 만든 key를 어떤 알고리즘으로 암호화 할 것인지
				.compact();
	}

	// Header에서 토큰 가져오기
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		if (bearerToken == null && request.getHeader("Cookie") != null) {
			String[] cookies = request.getHeader("Cookie").split("; ");
			for (String cookie : cookies) {
				String[] parsedCookie = cookie.split("=");
				String key = parsedCookie[0];
				String value = parsedCookie[1];

				if (key.equals("Authorization")) {
					bearerToken = value;
					break;
				}
			}
		}

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(7);
		}

		return null;
	}

	// 토큰 검증
	public void validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		} catch (SecurityException | MalformedJwtException e) {
			throw new IllegalArgumentException("유효하지 않은 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			throw new TokenExpiredException("만료된 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			throw new IllegalArgumentException("지원되지 않은 토큰입니다.");
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("잘못된 토큰입니다.");
		}
	}

	// 토큰에서 사용자 정보 가져오기
	public Claims getTokenClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
}