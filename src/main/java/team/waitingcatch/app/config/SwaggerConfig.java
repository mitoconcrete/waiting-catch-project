package team.waitingcatch.app.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.OAS_30)
			.securityContexts(Arrays.asList(securityContext()))
			.securitySchemes(Arrays.asList(apiKey()))
			.select()
			.apis(RequestHandlerSelectors.basePackage("team.waitingcatch.app.user.controller")
				.or(RequestHandlerSelectors.basePackage("team.waitingcatch.app.restaurant.controller"))
				.or(RequestHandlerSelectors.basePackage("team.waitingcatch.app.lineup.controller"))
				.or(RequestHandlerSelectors.basePackage("team.waitingcatch.app.event.controller"))
			)
			.paths(PathSelectors.ant("/api/**"))
			.build()
			.groupName("API 1.0.0") // group별 명칭을 주어야 한다.
			.pathMapping("/")
			.apiInfo(apiInfo())
			.useDefaultResponseMessages(false); // 400,404,500 .. 표기를 ui에서 삭제한다.
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title("웨이팅 캐치 프로젝트")
			.description("줄서기 솔루션 웨이팅 캐치 프로젝트의 api문서입니다.")
			.version("1.0.0")
			.termsOfServiceUrl("")
			.license("")
			.licenseUrl("")
			.build()
			;
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
			.securityReferences(defaultAuth())
			.build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
	}

	private ApiKey apiKey() {
		return new ApiKey("Authorization", "Authorization", "header");
	}
}