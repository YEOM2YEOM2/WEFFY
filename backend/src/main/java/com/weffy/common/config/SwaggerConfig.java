package com.weffy.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        final String securitySchemeName2 = "csrfToken";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName2))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name("JWT")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT"))
                                .addSecuritySchemes(securitySchemeName2,
                                        new SecurityScheme()
                                                .name("CSRF")
                                                .type(SecurityScheme.Type.APIKEY)
                                                .in(SecurityScheme.In.HEADER)
                                                .name("X-CSRF-TOKEN"))
                )
                .info(new Info()
                        .title("WEFFY")     /* 서비스 제목 */
                        .version("1.0.0")   /* 서비스 버전 */
                        .description("weffy는 학습 환경의 불편함을 해결하기 위한 새로운 솔루션입니다. \n\n 실시간 강의 스트리밍 기능, 화면 공유 기능, 원격 화면 공유, 등을 제공하며, 대규모 수업의 상호작용을 증진시키기 위해 익명 질문과 퀴즈 기능을 도입했습니다.\n\n 이를 통해 효과적인 학습을 지원하고, 교수와 학생간의 소통을 촉진하는 동시에 풍부한 학습 경험을 제공합니다."));    /* 서비스 설명 */
    }
}






