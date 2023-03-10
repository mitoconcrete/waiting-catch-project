package team.waitingcatch.app.config;

import java.util.List;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.JwtUtil;
import team.waitingcatch.app.redis.service.AliveTokenService;
import team.waitingcatch.app.redis.service.KilledAccessTokenService;
import team.waitingcatch.app.security.service.AccessDeniedHandlerImpl;
import team.waitingcatch.app.security.service.AuthenticationEntryPointImpl;
import team.waitingcatch.app.security.service.JwtAuthFilter;
import team.waitingcatch.app.user.enums.UserRoleEnum;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;
	private final AliveTokenService aliveTokenService;
	private final KilledAccessTokenService killedAccessTokenService;
	private final AccessDeniedHandlerImpl accessDeniedHandler;
	private final AuthenticationEntryPointImpl authenticationEntryPoint;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		String[] ignoreAuthorizationList = {"/general/templates/**", "/profile"};
		return (web) -> web.ignoring()
			.antMatchers(ignoreAuthorizationList)
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		String[] ignoreAuthorizationList = {"/api/general/**", "/general/templates/**", "/profile"};
		String[] customerOnlyAllowedList = {"/api/customer/**"};
		String[] sellerOnlyAllowedList = {"/api/seller/**", "/seller/templates/**"};
		String[] adminOnlyAllowedList = {"/admin/templates/**"};

		// jwt ????????? ??????, stateless??????.
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// CSRF ??????
		http.csrf().disable();

		//??????????????? ?????? ???, CORS ??? ???????????????.
		http.cors().configurationSource(request -> {

			var cors = new CorsConfiguration();
			// CORS ??? ????????? ????????? ????????? ????????? ????????? ???????????????.
			cors.setAllowedOrigins(List.of("https://waitingcatch.com", "https://www.waitingcatch.com"));
			cors.setAllowedMethods(List.of("*"));
			cors.setAllowedHeaders(List.of("*"));
			cors.setAllowCredentials(true);
			cors.addExposedHeader("Authorization");
			return cors;
		});

		// ?????? ??? ?????? ??????
		http.authorizeRequests()
			.antMatchers(adminOnlyAllowedList).hasRole(UserRoleEnum.ADMIN.toString())
			.antMatchers(sellerOnlyAllowedList).hasRole(UserRoleEnum.SELLER.toString())
			.antMatchers(customerOnlyAllowedList).hasRole(UserRoleEnum.USER.toString())
			.antMatchers(ignoreAuthorizationList).permitAll();

		// Custom ????????? ????????? ???????????? ??????.
		http.formLogin().disable();

		// Custom Filter ????????????
		http.addFilterBefore(
			new JwtAuthFilter(jwtUtil, userDetailsService, aliveTokenService, killedAccessTokenService),
			UsernamePasswordAuthenticationFilter.class);

		// 401 Error ??????, Authorization ???, ?????????????????? ????????? ??? ??????
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);

		// 403 Error ??????, ???????????? ????????? ???????????? ????????? ???????????? ?????? ??????
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

		return http.build();
	}
}