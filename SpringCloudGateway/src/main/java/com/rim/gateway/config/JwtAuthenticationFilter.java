package com.rim.gateway.config;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.rim.gateway.util.JwtUtil;
import com.rim.gateway.util.PermissionModel;
import com.rim.gateway.util.TokenBlacklistService;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    
    @Autowired
    private WebClient webClient;

    @Value("${auth.service.url}")
    private String authServiceUrl;


    
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    // 🔓 Public URLs
    private static final List<String> PUBLIC_URLS = List.of(

            // auth
            "/auth/authenticate/signin",
            "/auth/authenticate/refresh",
            "/auth/authenticate/forgot-password",
            "/auth/authenticate/reset-password",
            "/auth/authenticate/generateOtp",
            "/auth/authenticate/signup",
            
           

            // swagger
            "/auth/swagger-ui",
            "/auth/v3/api-docs",
            "/auth/swagger-ui.html",
            "/auth/v3/api-docs/swagger-config",

            // actuator
            "/actuator"
            
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        
        log.info("🔵 Gateway Request: {}", path);

        // ✅ allow OPTIONS (CORS preflight)
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        // ✅ skip public URLs
        if (isPublic(path)) {
        	
        	log.info("🟢 Skipping auth for public endpoint");

            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        
        log.info("🔐 Secured endpoint hit");

        // 🔐 validate token
        if (!jwtUtil.validateToken(token)) {
            return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
        }

        if (jwtUtil.isExpired(token)) {
            return onError(exchange, "Token expired", HttpStatus.UNAUTHORIZED);
        }
        
        if(tokenBlacklistService.isBlocked(token)){
            return onError(exchange, "User logged out", HttpStatus.UNAUTHORIZED);
        }


        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);
        
        //CheckPermission
        String resourceName = extractResource(path);
        String scopeName = extractScope(path);

        log.info("🔍 Permission check → Role={}, Resource={}, Scope={}",
                role, resourceName, scopeName);

        return checkPermission(role, resourceName, scopeName)
                .flatMap(hasPermission -> {

                    if (!hasPermission) {
                        log.error("⛔ Access Denied");
                        return onError(exchange, "Access Denied", HttpStatus.FORBIDDEN);
                    }

                    log.info("✅ Permission granted");

                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                            .mutate()
                            .header("X-User-Email", email)
                            .header("X-User-Role", role)
                            .header("Secret", "AbhishekMH")
                            .build();

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                });
    }

    private boolean isPublic(String path) {

        for (String url : PUBLIC_URLS) {
            if (path.startsWith(url)) {
                log.info("🟢 Public URL: {}", path);
                return true;
            }
        }

        return false;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String msg, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(msg.getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1;
    }
    
    private String extractResource(String path) {

        if (path == null || path.isEmpty())
            return null;

        String[] parts = path.split("/");

        // /user/create → ["", "user", "create"]
        if (parts.length > 1) {
            return parts[2].toUpperCase();
        }

        return null;
    }

    private String extractScope(String path) {

        if (path == null || path.isEmpty())
            return null;

        String[] parts = path.split("/");

        // last segment is scope
        if (parts.length > 2) {
            return parts[parts.length - 1].toUpperCase();
        }

        return null;
    }
    
    private Mono<Boolean> checkPermission(String role, String resource, String scope) {

        String url = authServiceUrl + "/permission/check";

        PermissionModel body = new PermissionModel();
        body.setRoleName(role);
        body.setResourceName(resource);
        body.setScopeName(scope);
        //grant permission for logout API
        if(resource.equalsIgnoreCase("authenticate")&&scope.equalsIgnoreCase("logout")) {
        	 return Mono.just(true);
        }

        return webClient.post()
                .uri(url)
                .header("Secret", "AbhishekMH")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(resp -> {

                    Object statusObj = resp.get("status");
                    Object dataObj = resp.get("data");

                    boolean status = statusObj instanceof Boolean && (Boolean) statusObj;
                    boolean permission = dataObj instanceof Boolean && (Boolean) dataObj;

                    // 🔴 if status false → deny
                    if (!status) {
                        log.error("Permission API returned status=false");
                        return false;
                    }

                    return permission;
                })
                .onErrorResume(e -> {
                    log.error("Permission API failed: {}", e.getMessage());
                    return Mono.just(false);
                });
    }

}
