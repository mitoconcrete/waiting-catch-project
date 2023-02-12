package team.waitingcatch.app.config;

import java.util.List;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.utils.JwtUtil;
import team.waitingcatch.app.security.service.AccessDeniedHandlerImpl;
import team.waitingcatch.app.security.service.AuthenticationEntryPointImpl;
import team.waitingcatch.app.security.service.JwtAuthFilter;
import team.waitingcatch.app.user.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;
	private AccessDeniedHandlerImpl accessDeniedHandler;
	private AuthenticationEntryPointImpl authenticationEntryPoint;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// CSRF 설정
		http.csrf().disable();

		// 프론트엔드 접속 시, CORS 를 허용합니다.
		http.cors().configurationSource(request -> {
			var cors = new CorsConfiguration();
			// CORS 를 허용할 주소를 아래에 리스트 형태로 넣어주세요.
			cors.setAllowedOrigins(List.of("http://localhost:3000"));
			cors.setAllowedMethods(List.of("*"));
			cors.setAllowedHeaders(List.of("*"));
			cors.setAllowCredentials(true);
			return cors;
		});

		http.authorizeRequests().anyRequest().permitAll();

		// Custom 로그인 페이지 사용하지 않음.
		http.formLogin().disable();

		// Custom Filter 등록하기
		http.addFilterBefore(new JwtAuthFilter(jwtUtil, userDetailsService),
			UsernamePasswordAuthenticationFilter.class);

		// 401 Error 처리, Authorization 즉, 인증과정에서 실패할 시 처리
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);

		// 403 Error 처리, 인증과는 별개로 추가적인 권한이 충족되지 않는 경우
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

		return http.build();
	}

}
