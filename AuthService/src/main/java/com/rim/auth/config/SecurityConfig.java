package com.rim.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Autowired
    private RequestLoggingFilter requestLoggingFilter;
    
    @Autowired
    private InternalSecretFilter internalSecretFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Custom entry point to prevent browser popup
        AuthenticationEntryPoint customAuthEntryPoint = (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Unauthorized\"}");
        };

        AccessDeniedHandler customAccessDeniedHandler = (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Forbidden\"}");
        };

        http
            .csrf(csrf -> csrf.disable())
            // 🔹 FIRST → check internal secret
            .addFilterBefore(internalSecretFilter, UsernamePasswordAuthenticationFilter.class)

            // 🔹 Logging filter (optional)
            .addFilterBefore(requestLoggingFilter, InternalSecretFilter.class)

            .authorizeHttpRequests(auth -> auth

                // 🔓 AUTH APIs
                .requestMatchers("/user/create").permitAll()
                
                // 🔓 Temp permit
                .requestMatchers("/role/**").permitAll()
                
                // 🔓 SWAGGER
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/swagger-config",
                        "/swagger-ui/index.html"
                        
                ).permitAll()

                // 🔒 everything else
                .anyRequest().permitAll()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(customAuthEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
            )
            .httpBasic(httpBasic -> httpBasic.disable())   // ❗ prevent browser popup
            .formLogin(form -> form.disable());  

        return http.build();
    }
}
