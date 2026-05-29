package dev.jinulab.isetdx.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
	    info = @Info(
	        title       = "JWT 인증 API 명세서",
	        version     = "v1.0",
	        description = """
	            ## JWT 기반 사용자 인증 REST API

	            ### 🔑 Swagger 테스트 순서
	            1. `POST /api/auth/send-code` — 이메일로 인증 코드 발송
	            2. `POST /api/auth/register` — 인증 코드 포함 회원가입
	            3. `POST /api/auth/login` — 로그인 → **accessToken** 발급
	            4. 우측 상단 **Authorize 🔒 버튼** 클릭
	            5. `Bearer Authentication` 항목에 accessToken **값만** 입력 (Bearer 제외)
	            6. 인증이 필요한 API(`🔒` 표시) 테스트 진행

	            ### ⚠️ 토큰 유효시간
	            - Access Token : **30분**
	            - Refresh Token : **7일**
	            """,
	        contact = @Contact(
	            name  = "API 관리자",
	            email = "admin@example.com"
	        )
	    ),
	    servers = {
	        @Server(url = "http://localhost:8080", description = "로컬 개발 서버")
	    }
	)
	@SecurityScheme(
	    name        = "Bearer Authentication",
	    type        = SecuritySchemeType.HTTP,
	    scheme      = "bearer",
	    bearerFormat = "JWT",
	    description = "로그인 후 발급된 accessToken을 입력하세요. (예: eyJhbGci...)"
	)
	@Configuration
	public class SwaggerConfig {

	//2026-02-25 L.JW: 전체 API 정보를 보여주기위해 주석처리하
//	    /** 인증/회원 API 그룹 */
//	    @Bean
//	    public GroupedOpenApi authGroup() {
//	        return GroupedOpenApi.builder()
//	                .group("1. 인증 API")
//	                .pathsToMatch("/api/auth/**")
//	                .build();
//	    }
//
//	    /** 관리자 API 그룹 */
//	    @Bean
//	    public GroupedOpenApi adminGroup() {
//	        return GroupedOpenApi.builder()
//	                .group("2. 관리자 API")
//	                .pathsToMatch("/api/admin/**")
//	                .build();
//	    }
//
//	    /** 전체 API 그룹 */
//	    @Bean
//	    public GroupedOpenApi allGroup() {
//	        return GroupedOpenApi.builder()
//	                .group("0. 전체 API")
//	                .pathsToMatch("/api/**")
//	                .build();
//	    }
	}
