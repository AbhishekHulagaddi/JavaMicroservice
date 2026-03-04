package com.rim.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // ❌ Custom 401/403 handling (no browser popup)
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((exchange, exAuth) -> {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        DataBuffer buffer = exchange.getResponse()
                                .bufferFactory()
                                .wrap("{\"error\":\"Unauthorized\"}".getBytes());
                        return exchange.getResponse().writeWith(Mono.just(buffer));
                    })
                    .accessDeniedHandler((exchange, exDenied) -> {
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        DataBuffer buffer = exchange.getResponse()
                                .bufferFactory()
                                .wrap("{\"error\":\"Forbidden\"}".getBytes());
                        return exchange.getResponse().writeWith(Mono.just(buffer));
                    })
                )

                .authorizeExchange(ex -> ex

                        // 🔓 AUTH APIs
                        .pathMatchers("/auth/authenticate/**").permitAll()
                        
                        // 🔓 Temp permit
                        .pathMatchers("/auth/role/**").permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll() 
                        // 🔓 SWAGGER via gateway
                        .pathMatchers(
                                "/auth/v3/api-docs/**",
                                "/auth/swagger-ui/**",
                                "/auth/swagger-ui.html"
                        ).permitAll()

                        // 🔒 everything else
                        .anyExchange().permitAll()
                )
                .build();
    }
}
