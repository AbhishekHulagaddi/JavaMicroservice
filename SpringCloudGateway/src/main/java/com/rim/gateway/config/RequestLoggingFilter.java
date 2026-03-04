package com.rim.gateway.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
@Slf4j
public class RequestLoggingFilter {

    @Bean
    public WebFilter logRequestFilter() {
        return (exchange, chain) -> {

            String path = exchange.getRequest().getURI().getPath();
            String method = exchange.getRequest().getMethod().name();

            log.info("🌐 Gateway Request → {} {}", method, path);

            return chain.filter(exchange);
        };
    }
}
