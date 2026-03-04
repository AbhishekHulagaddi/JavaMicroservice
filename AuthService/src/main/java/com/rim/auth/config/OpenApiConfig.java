package com.rim.auth.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
            .info(new Info()
                .title("TMS - Auth Service")
                .version("1.0")
                .description("Auth Service for United TMS Applications"))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))  // ✅ Apply globally
            .components(new Components().addSecuritySchemes(securitySchemeName,
                new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            ))
            .servers(List.of(
                new Server().url("/auth").description("Auth Service via Gateway"),
                new Server().url("http://localhost:9201").description("Direct Auth Service (bypass Gateway)")
            ));
    }
}